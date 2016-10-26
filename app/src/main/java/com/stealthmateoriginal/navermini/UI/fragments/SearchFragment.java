package com.stealthmateoriginal.navermini.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.MainActivity;
import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DictionarySpinnerAdapter;
import com.stealthmateoriginal.navermini.UI.ResultListAdapter;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.SearchEngine;
import com.stealthmateoriginal.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class SearchFragment extends Fragment {

    private class OnSelectSubdictionaryListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            state.setCurrentSubDictionary(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class OnSelectDictionaryListener implements  AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setSubDictionaryList(dictAdapter.getItem(position).resultListDictionary);
            state.setCurrentDictionary(dictAdapter.getItem(position).resultListDictionary);
            subdictList.setSelection(0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private StateManager state;
    private LinearLayout resultcontainer;
    private Spinner subdictList;
    private Spinner dictList;
    private DictionarySpinnerAdapter dictAdapter;

    private boolean created = false;

    private LinearLayout loadingView;

    public LinearLayout getResultContainer() {
        return resultcontainer;
    }

    public void performSearch() {

        if(!created) return;

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( getView().getWindowToken(), 0);

        getView().requestLayout();

        ViewGroup root = (ViewGroup) this.getView();
        EditText searchBar = (EditText) root.findViewById(R.id.search_bar);
        final String query = searchBar.getText().toString();

        if(query.length() == 0) return;

        state.query(query, 1, 10, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                populate(ResultListAdapter.mapFromSearch(state, state.getCurrentSubDictionary(), query, response));
            }
        });
        this.waitForResults();
    }

    public void populate(final ResultListAdapter adapter) {

        if(adapter.getQuery().length() == 0) return;
        ListView resultslist = (ListView) this.getView().findViewById(R.id.resultsListView);
        resultslist.setAdapter(adapter);
        resultslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClicked(view, position, id);
            }
        });

        loadingView.setVisibility(View.GONE);
    }

    public void waitForResults() {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void clear() {
        if(resultcontainer == null) return;
        resultcontainer = (LinearLayout) getActivity().findViewById(R.id.result);
        ListView v = (ListView) resultcontainer.findViewById(R.id.resultsListView);
        v.setAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.state = ((MainActivity) getActivity()).getState();

        final View contents = inflater.inflate(R.layout.fragment_search, container, false);

        resultcontainer = (LinearLayout) contents.findViewById(R.id.result);
        ViewGroup.inflate(getActivity(), R.layout.layout_results, resultcontainer);

        this.dictList = (Spinner) contents.findViewById(R.id.view_search_select_dict);
        this.dictAdapter = new DictionarySpinnerAdapter(getContext());
        this.dictList.setAdapter(this.dictAdapter);
        this.dictList.setOnItemSelectedListener(new OnSelectDictionaryListener());

        this.subdictList = (Spinner) contents.findViewById(R.id.spinner_select_enigne);
        this.subdictList.setOnItemSelectedListener(new OnSelectSubdictionaryListener());

        EditText searchBox = (EditText) contents.findViewById(R.id.search_bar);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch();
                return true;
            }
        });

        this.loadingView = (LinearLayout) contents.findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        ViewGroup searchBar = (ViewGroup) contents.findViewById(R.id.view_search_searchbar);
        //searchBar.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ViewGroup fragContainer = (ViewGroup) contents.findViewById(R.id.view_search_container);
        //fragContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        created = true;

        return contents;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This is the NoSaveStateFrameLayout - force it to not clip
        FrameLayout frameLayout = (FrameLayout) getView();
        frameLayout.setClipChildren(false);
        frameLayout.setClipToPadding(false);
        setSubDictionaryList(state.getCurrentDictionary());
    }

    public void setSubDictionaryList(ResultListDictionary dict) {
        ResultListDictionary.SubDictionary[] subdicts = dict.subdicts;
        ArrayList<String> subdictStrings = new ArrayList<>(subdicts.length);
        for(int i=0;i<=subdicts.length-1;i++) {
            subdictStrings.add(subdicts[i].name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subdictStrings);
        subdictList.setAdapter(adapter);
    }
}
