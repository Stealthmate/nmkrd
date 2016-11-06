package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static android.R.attr.data;
import static android.content.ContentValues.TAG;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public class ResultListSearchVisualizer extends SearchVisualizer {

    public static ResultListSearchVisualizer mapFromSearch(StateManager state, ResultListQuery query, String response) {

        ArrayList<Entry> entries = query.getSubDictionary().translator.translate(response);
        if(entries.size() == 0) return new ResultListSearchVisualizer(state, null);
        NetworkEntryListAdapter adapter = new NetworkEntryListAdapter(state, entries, query);

        return new ResultListSearchVisualizer(state.getActivity(), adapter);
    }

    private NetworkEntryListAdapter adapter;

    public ResultListSearchVisualizer(Context context, NetworkEntryListAdapter adapter) {
        super(context);
        this.adapter = adapter;
    }

    public ResultListSearchVisualizer(StateManager state, Serializable data) {
        super(state.getActivity());
        this.adapter = (NetworkEntryListAdapter) EntryListAdapter.deserialize(state, (EntryListAdapter.SerializableRepresentation) data);
    }

    public void setAdapter(NetworkEntryListAdapter adapter) {
        this.adapter = adapter;
    }

    public NetworkEntryListAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    public View getView(@NonNull ViewGroup container) {

        if (adapter == null) {
            return LayoutInflater.from(getContext()).inflate(R.layout.view_no_results, container, false);
        }

        ListView view = (ListView) LayoutInflater.from(getContext()).inflate(R.layout.layout_results, container, false);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClicked(position);
            }
        });
        return view;
    }

    @Override
    public Serializable getDataRepresentation() {
        return adapter.getDataRepresentation();
    }
}
