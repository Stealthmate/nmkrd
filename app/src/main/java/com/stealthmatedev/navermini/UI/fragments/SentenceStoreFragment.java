package com.stealthmatedev.navermini.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.ResultListSearchVisualizer;
import com.stealthmatedev.navermini.data.history.HistoryManager;
import com.stealthmatedev.navermini.data.sentencestore.SentenceStoreManager;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public class SentenceStoreFragment extends Fragment {

    private ListView list;
    private View loadingView;
    private StateManager state;



    public void performSearch() {

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        getView().requestLayout();

        ViewGroup root = (ViewGroup) this.getView();
        EditText searchBar = (EditText) root.findViewById(R.id.search_bar);
        final String querystring = searchBar.getText().toString();

        if (querystring.length() == 0) return;

        state.sentenceStore().queryByWord(querystring, new SentenceStoreManager.Callback() {
            @Override
            public void callback(Object result) {
                list.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_minimal, (ArrayList<String>)result));
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    private void waitForData() {
        loadingView.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sentence_store, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        this.loadingView = this.getView().findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        this.state = StateManager.getState(getContext());

        EditText searchBox = (EditText) getView().findViewById(R.id.search_bar);
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                performSearch();
                return true;
            }
        });


        this.list = (ListView) getView().findViewById(R.id.result);
        this.state = StateManager.getState(getContext());
    }

}
