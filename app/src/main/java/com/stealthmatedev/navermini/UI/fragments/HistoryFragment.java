package com.stealthmatedev.navermini.UI.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.ArrayListEntryProvider;
import com.stealthmatedev.navermini.UI.CursorEntryProvider;
import com.stealthmatedev.navermini.UI.HistoryEntryListAdapter;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryEntry;
import com.stealthmatedev.navermini.data.history.HistoryManager;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.entries;
import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/10/26 0026.
 */

public class HistoryFragment extends Fragment {

    private StateManager state;

    private View loadingView;
    private ListView list;

    private void populate(ArrayList<Entry> data) {
        final HistoryEntryListAdapter adapter = new HistoryEntryListAdapter(state, new ArrayListEntryProvider(data));
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClicked(position);
            }
        });
        loadingView.setVisibility(View.GONE);
    }

    private void waitForData() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        this.loadingView = this.getView().findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        this.state = StateManager.getState(getContext());

        this.state.history().registerObserver(new HistoryManager.Observer() {
            @Override
            public void onChanged() {
                update();
            }

            @Override
            public void onInvalidated() {
                update();
            }
        });

        this.waitForData();

        this.list = (ListView) getView().findViewById(R.id.view_history_list);
        final StateManager state = StateManager.getState(getContext());
        this.update();
    }

    public void update() {
        state.history().getAll(new HistoryManager.Callback() {
            @Override
            public void onFinish(Object result) {
                ArrayList<Entry> arr = new ArrayList<Entry>((ArrayList<DetailedEntry>) result);
                populate(arr);
            }
        });
    }

}
