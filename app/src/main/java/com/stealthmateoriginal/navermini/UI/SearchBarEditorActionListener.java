package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.kr.KrWordsAdapter;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.SearchEngine;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONObject;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class SearchBarEditorActionListener implements TextView.OnEditorActionListener {

    private StateManager state;

    public SearchBarEditorActionListener(StateManager state) {
        this.state = state;
    }

    @Override
    public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) state.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( state.getActivity().findViewById(android.R.id.content).getWindowToken(), 0);

        state.query(v.getText().toString(), 1, 10, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                state.getSearchFragment().populate(ResultListAdapter.mapFromSearch(state, state.getCurrentSubDictionary(), v.getText().toString(), response));
            }
        });
        state.getSearchFragment().waitForResults();
        return true;
    }
}
