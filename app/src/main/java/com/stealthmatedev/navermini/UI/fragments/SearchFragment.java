package com.stealthmatedev.navermini.UI.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DictionarySpinnerAdapter;
import com.stealthmatedev.navermini.UI.NetworkEntryListAdapter;
import com.stealthmatedev.navermini.UI.specific.ACSuggestionAdapter;
import com.stealthmatedev.navermini.data.AutocompleteSuggestion;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.SentenceEntry;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;
import com.stealthmatedev.navermini.state.Autocompleter;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class SearchFragment extends Fragment {

    private class OnSelectSubdictionaryListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setCurrentSubDictionary(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSelectDictionaryListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setCurrentDictionary(dictAdapter.getItem(position).entryListDictionary);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static final String SAVE_KEY_DICT = "dict";
    private static final String SAVE_KEY_SUBDICT = "subdict";
    private static final String SAVE_KEY_ADAPTER = "adapter";

    private EntryListDictionary currentDictionary;
    private EntryListDictionary.SubDictionary currentSubDictionary;

    private StateManager state;
    private ListView resultcontainer;
    private View loadingView;

    private AutoCompleteTextView searchBox;
    private View acloadingView;

    private Spinner subdictList;
    private Spinner dictList;

    private DictionarySpinnerAdapter dictAdapter;

    private NetworkEntryListAdapter currentAdapter;

    private boolean created = false;

    public void setCurrentSubDictionary(int i) {
        if (i < 0 || i >= currentDictionary.subdicts.length) {
            throw new IllegalArgumentException("Invalid sub-dictionary index: " + i + " (sub-dictionaries of " + currentDictionary.name() + ": " + currentDictionary.subdicts.length);
        }
        this.currentSubDictionary = currentDictionary.subdicts[i];
    }

    public void setCurrentDictionary(EntryListDictionary dict) {
        this.currentDictionary = dict;
        setSubDictionaryList(this.currentDictionary);
    }

    private void setSubDictionaryList(EntryListDictionary dict) {
        EntryListDictionary.SubDictionary[] subdicts = dict.subdicts;
        ArrayList<String> subdictStrings = new ArrayList<>(subdicts.length);
        for (int i = 0; i <= subdicts.length - 1; i++) {
            subdictStrings.add(getContext().getResources().getString(subdicts[i].name));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subdictStrings);
        subdictList.setAdapter(adapter);
        subdictList.setSelection(0);
        setCurrentSubDictionary(0);
    }

    public void performSearch() {

        if (!created) return;

        searchBox.dismissDropDown();

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        getView().requestLayout();

        ViewGroup root = (ViewGroup) this.getView();
        EditText searchBar = (EditText) root.findViewById(R.id.search_bar);
        final String querystring = searchBar.getText().toString();
        String path = currentDictionary.path + currentSubDictionary.path;

        if (querystring.length() == 0) return;

        final ResultListQuery query = new ResultListQuery(path, querystring, 1, 10, currentSubDictionary);

        state.getSearchEngine().queryResultList(query, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                clear();
                populate(NetworkEntryListAdapter.mapFromSearch(state, query, response));
            }

            @Override
            public void onError(VolleyError err) {
                clear();
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onRedirect() {

            }
        });
        this.waitForResults();
    }

    public void populate(final NetworkEntryListAdapter visualizer) {

        if (visualizer != null) this.currentAdapter = visualizer;

        if (this.resultcontainer == null) return;

        clear();

        if (this.currentAdapter == null) return;

        resultcontainer.setAdapter(visualizer);
        resultcontainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentAdapter.onItemClicked(position);
            }
        });


        this.loadingView.setVisibility(View.GONE);
        this.resultcontainer.setVisibility(View.VISIBLE);

    }

    public void waitForResults() {
        if (resultcontainer == null) return;
        clear();
        this.loadingView.setVisibility(View.VISIBLE);
        this.resultcontainer.setVisibility(View.GONE);
    }

    public void clear() {
        if (resultcontainer == null) return;
        resultcontainer.removeAllViewsInLayout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dictAdapter = new DictionarySpinnerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntryListDictionary dict = null;
        EntryListDictionary.SubDictionary subdict = null;

        if (savedInstanceState != null) {
            dict = EntryListDictionary.valueOf(savedInstanceState.getString(SAVE_KEY_DICT));
            if (dict != null) {
                subdict = dict.getSubDict(container.getContext(), savedInstanceState.getString(SAVE_KEY_SUBDICT));
            }
        }

        if (dict != null) {
            this.currentDictionary = dict;
        } else {
            this.currentDictionary = this.dictAdapter.getItem(0).entryListDictionary;
        }

        if (subdict != null && this.currentDictionary.indexOf(subdict) >= 0) {
            this.currentSubDictionary = subdict;
        } else {
            this.currentSubDictionary = this.currentDictionary.subdicts[0];
        }

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.resultcontainer = (ListView) view.findViewById(R.id.result);
        registerForContextMenu(resultcontainer);

        this.dictList = (Spinner) view.findViewById(R.id.view_search_select_dict);
        this.subdictList = (Spinner) view.findViewById(R.id.spinner_select_enigne);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.state = ((MainActivity) getActivity()).getState();

        this.loadingView = getView().findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        this.dictList.setAdapter(this.dictAdapter);
        this.dictList.setOnItemSelectedListener(new OnSelectDictionaryListener());
        this.subdictList.setOnItemSelectedListener(new OnSelectSubdictionaryListener());
        setCurrentDictionary(this.currentDictionary);
        setCurrentSubDictionary(this.currentDictionary.indexOf(this.currentSubDictionary));


        this.acloadingView = getView().findViewById(R.id.view_search_autocomplete_loading);

        searchBox = (AutoCompleteTextView) getView().findViewById(R.id.search_bar);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch();
                return true;
            }
        });
        searchBox.setFilters(new InputFilter[]{});
        searchBox.setAdapter(new ACSuggestionAdapter(getContext(), new ArrayList<AutocompleteSuggestion>()));
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                performSearch();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(currentDictionary.autocompleter == null) return;
                acloadingView.setVisibility(View.VISIBLE);

                currentDictionary.autocompleter.getSuggestions(state, s.toString(), new Autocompleter.OnSuggestions() {
                    @Override
                    public void received(final ArrayList<AutocompleteSuggestion> suggestions) {
                        final ArrayAdapter<AutocompleteSuggestion> adapter = (ACSuggestionAdapter) searchBox.getAdapter();
                        acloadingView.setVisibility(View.GONE);
                        adapter.clear();
                        adapter.addAll(suggestions);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(VolleyError error) {
                        acloadingView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        created = true;

        if (savedInstanceState != null) {
            NetworkEntryListAdapter vis = NetworkEntryListAdapter.fromSavedState(state, savedInstanceState);
            if (vis != null) this.currentAdapter = vis;
        }

        populate(this.currentAdapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        int pos = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        Entry entry = (Entry) currentAdapter.getItem(pos);

        if(entry instanceof SentenceEntry) {
            super.onCreateContextMenu(menu, view, menuInfo);
            menu.add(Menu.NONE, 0, 0, R.string.label_menu_save);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                AdapterView.AdapterContextMenuInfo minfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                SentenceEntry entry = (SentenceEntry) this.currentAdapter.getItem(minfo.position);
                state.dbhelper().sentenceStore().put(entry, null);
            }

            break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentAdapter != null) {
            outState.putSerializable(SAVE_KEY_ADAPTER, currentAdapter.getDataRepresentation());
        }
        outState.putString(SAVE_KEY_DICT, currentDictionary.name());
        outState.putString(SAVE_KEY_SUBDICT, getContext().getResources().getString(currentSubDictionary.name));
    }

}
