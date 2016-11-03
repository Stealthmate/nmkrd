package com.stealthmatedev.navermini.UI;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.data.Entry;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class EntryListAdapter extends BaseListAdapter {

    private EntryVisualizer visualizer;
    private EntryProvider entryProvider;

    public EntryListAdapter(EntryProvider entryProvider, EntryVisualizer visualizer) {

        this.entryProvider = entryProvider;
        this.entryProvider.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                EntryListAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                EntryListAdapter.this.notifyDataSetInvalidated();
            }
        });
        this.visualizer = visualizer;

    }

    public void setVisualizer(EntryVisualizer visualizer) {
        this.visualizer = visualizer;
        notifyDataSetChanged();
    }

    public EntryVisualizer getVisualizer() {
        return this.visualizer;
    }

    @Override
    protected int getItemCountInternal() {
        return entryProvider.getCount();
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        Log.i(APPTAG, String.valueOf(visualizer == null));
        return visualizer.visualize((Entry) getItem(position), parent);
    }

    @Override
    protected boolean onClick(int position) {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return entryProvider.getEntry(position);
    }

    @Override
    public long getItemId(int position) {
        return entryProvider.getEntry(position).hashCode();
    }
}
