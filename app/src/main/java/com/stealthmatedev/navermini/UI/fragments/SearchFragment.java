package com.stealthmatedev.navermini.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DictionarySpinnerAdapter;
import com.stealthmatedev.navermini.UI.ResultListSearchVisualizer;
import com.stealthmatedev.navermini.UI.SearchVisualizer;
import com.stealthmatedev.navermini.state.ResultListDictionary;
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
            setCurrentDictionary(dictAdapter.getItem(position).resultListDictionary);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static final String SAVE_KEY_DICT = "dict";
    private static final String SAVE_KEY_SUBDICT = "subdict";

    private ResultListDictionary currentDictionary;
    private ResultListDictionary.SubDictionary currentSubDictionary;

    private StateManager state;
    private LinearLayout resultcontainer;
    private Spinner subdictList;
    private Spinner dictList;
    private DictionarySpinnerAdapter dictAdapter;

    private SearchVisualizer currentVisualizer;

    private boolean created = false;


    public void setCurrentSubDictionary(int i) {
        if (i < 0 || i >= currentDictionary.subdicts.length) {
            throw new IllegalArgumentException("Invalid sub-dictionary index: " + i + " (sub-dictionaries of " + currentDictionary.name() + ": " + currentDictionary.subdicts.length);
        }
        this.currentSubDictionary = currentDictionary.subdicts[i];
    }

    public void setCurrentDictionary(ResultListDictionary dict) {
        this.currentDictionary = dict;
        setSubDictionaryList(this.currentDictionary);
    }

    private void setSubDictionaryList(ResultListDictionary dict) {
        ResultListDictionary.SubDictionary[] subdicts = dict.subdicts;
        ArrayList<String> subdictStrings = new ArrayList<>(subdicts.length);
        for (int i = 0; i <= subdicts.length - 1; i++) {
            subdictStrings.add(subdicts[i].name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subdictStrings);
        subdictList.setAdapter(adapter);
        subdictList.setSelection(0);
        setCurrentSubDictionary(0);
    }

    public void performSearch() {

        if (!created) return;

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        getView().requestLayout();

        ViewGroup root = (ViewGroup) this.getView();
        EditText searchBar = (EditText) root.findViewById(R.id.search_bar);
        final String querystring = searchBar.getText().toString();
        String path = currentDictionary.path + currentSubDictionary.path;

        if (querystring.length() == 0) return;

        final ResultListQuery query = new ResultListQuery(path, querystring, 1, 10);

        state.getSearchEngine().queryResultList(query, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                populate(ResultListSearchVisualizer.mapFromSearch(state, currentSubDictionary, query, response));
            }

            @Override
            public void onError(VolleyError err) {
                clear();
            }
        });
        this.waitForResults();
    }

    public void populate(final SearchVisualizer visualizer) {

        if (visualizer != null) this.currentVisualizer = visualizer;

        if (this.resultcontainer == null) return;
        if (this.currentVisualizer == null) return;

        clear();
        resultcontainer.addView(currentVisualizer.getView(resultcontainer));
    }

    public void waitForResults() {
        if (resultcontainer == null) return;
        clear();
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this.resultcontainer, true);
    }

    public void clear() {
        if (resultcontainer == null) return;
        resultcontainer.removeAllViewsInLayout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dictAdapter = new DictionarySpinnerAdapter();

        ResultListDictionary dict = null;
        ResultListDictionary.SubDictionary subdict = null;

        if (savedInstanceState != null) {
            System.out.println(savedInstanceState);
            dict = ResultListDictionary.valueOf(savedInstanceState.getString(SAVE_KEY_DICT));
            if (dict != null) {
                subdict = dict.getSubDict(savedInstanceState.getString(SAVE_KEY_SUBDICT));
            }
        }

        if (dict != null) {
            this.currentDictionary = dict;
        } else {
            this.currentDictionary = this.dictAdapter.getItem(0).resultListDictionary;
        }

        if (subdict != null && this.currentDictionary.indexOf(subdict) >= 0) {
            this.currentSubDictionary = subdict;
        } else {
            this.currentSubDictionary = this.currentDictionary.subdicts[0];
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.resultcontainer = (LinearLayout) view.findViewById(R.id.result);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_results, resultcontainer, true);

        this.dictList = (Spinner) view.findViewById(R.id.view_search_select_dict);
        this.subdictList = (Spinner) view.findViewById(R.id.spinner_select_enigne);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.state = ((MainActivity) getActivity()).getState();

        this.dictList.setAdapter(this.dictAdapter);
        this.dictList.setOnItemSelectedListener(new OnSelectDictionaryListener());
        this.subdictList.setOnItemSelectedListener(new OnSelectSubdictionaryListener());
        setCurrentDictionary(this.currentDictionary);
        setCurrentSubDictionary(this.currentDictionary.indexOf(this.currentSubDictionary));


        EditText searchBox = (EditText) getView().findViewById(R.id.search_bar);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch();
                return true;
            }
        });

        created = true;

        if (savedInstanceState != null) {
            SearchVisualizer vis = ResultListSearchVisualizer.fromSavedState(state, savedInstanceState);
            if (vis != null) this.currentVisualizer = vis;
        }

        populate(this.currentVisualizer);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentVisualizer != null) {
            currentVisualizer.saveState(outState);
            System.out.println("DICT!");
            System.out.println(currentDictionary);
            outState.putString(SAVE_KEY_DICT, currentDictionary.name());
            outState.putString(SAVE_KEY_SUBDICT, currentSubDictionary.name);
        }
    }

}
