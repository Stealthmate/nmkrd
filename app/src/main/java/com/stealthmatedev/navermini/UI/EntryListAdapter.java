package com.stealthmatedev.navermini.UI;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class EntryListAdapter extends BaseAdapter implements ListAdapter {

    protected static class SerializableRepresentation implements Serializable {

        protected final Class<? extends EntryListAdapter> childClass;

        protected SerializableRepresentation(Class<? extends EntryListAdapter> childClass) {
            this.childClass = childClass;
        }
    }

    static EntryListAdapter deserialize(StateManager state, SerializableRepresentation repr) {
        if (repr == null) return null;

        Class<? extends EntryListAdapter> _class = repr.childClass;
        if (_class == null) return null;

        try {
            return _class.getConstructor(StateManager.class, SerializableRepresentation.class).newInstance(state, repr);
        } catch (NoSuchMethodException e) {
            Log.e(APPTAG, "Cannot instantiate DetailedItemListAdapter: Constructor from SerializableRepresentation not declared in " + _class.getSimpleName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

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
    public int getCount() {
        return entryProvider.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

    public boolean onItemClicked(int position) {
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

    public Serializable getDataRepresentation() {
        return new EntryListAdapter.SerializableRepresentation(this.getClass());
    }

    @Override
    public final boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public final boolean isEnabled(int position) {
        return true;
    }

}
