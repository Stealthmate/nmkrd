package com.stealthmatedev.navermini.UI.en.search;

import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public class EnWordsAdapter extends ResultListAdapter {
    public EnWordsAdapter(StateManager state, String query, String response) {
        super(state, query, response);
    }

    public EnWordsAdapter(StateManager state, Serializable data) {
        super(state, data);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        return null;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
