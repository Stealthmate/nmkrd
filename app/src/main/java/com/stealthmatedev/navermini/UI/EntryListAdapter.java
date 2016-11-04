package com.stealthmatedev.navermini.UI;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class EntryListAdapter extends BaseListAdapter {

    private EntryProvider entryProvider;

    public EntryListAdapter(EntryProvider entryProvider) {

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

    }

    protected EntryProvider getEntryProvider() {
        return this.entryProvider;
    }

    @Override
    protected int getItemCountInternal() {
        return entryProvider.getCount();
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {

        Entry entry = (Entry) getItem(position);

        EntryVisualizer visualizer = null;
        try {
            visualizer = EntryUIMapper.forEntry(entry).entryVisualizerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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
