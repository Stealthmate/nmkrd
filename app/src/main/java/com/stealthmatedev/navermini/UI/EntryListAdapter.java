package com.stealthmatedev.navermini.UI;

import android.database.DataSetObserver;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static android.R.attr.data;
import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class EntryListAdapter extends BaseAdapter implements ListAdapter {

    protected static class SerializableRepresentation implements Serializable {

        protected final Class<? extends EntryListAdapter> childClass;
        protected final Serializable provider;
        protected final Serializable data;

        protected SerializableRepresentation(Class<? extends EntryListAdapter> childClass, Serializable providerRepresentation, Serializable data) {
            this.childClass = childClass;
            this.provider = providerRepresentation;
            this.data = data;
        }
    }

    static EntryListAdapter deserialize(StateManager state, SerializableRepresentation repr) {
        if (repr == null) return null;

        Class<? extends EntryListAdapter> _class = repr.childClass;

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

        Log.e(APPTAG, "COULD NOT DESERIALIZE ENTRYLISTADAPTER! " + repr.childClass);

        return null;

    }

    private EntryProvider entryProvider;

    protected EntryListAdapter(SerializableRepresentation repr) {
        this.entryProvider = EntryProvider.deserialize((EntryProvider.EntryProviderRepresentation) repr.provider);
    }

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

    public void setEntryProvider(EntryProvider provider) {
        this.entryProvider = provider;
        notifyDataSetChanged();
    }

    public EntryProvider getEntryProvider() {
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
            EntryUIMapper.EntryUI visClass = EntryUIMapper.forEntry(entry);
            if(visClass == null) {
                Log.e(APPTAG, "Could not find EntryVisualizer for class " + entry.getClass().getName());
                Toast.makeText(parent.getContext(), "Internal Error!", Toast.LENGTH_SHORT);
                return new Space(parent.getContext());
            }
            visualizer = visClass.entryVisualizerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }

        try {
            return visualizer.visualize(entry, parent);
        } catch (Exception ex) {
            Toast.makeText(parent.getContext(), "Error! Encountered corrupted item at position " + position + " (not displayed). Will be removed from history if exists.", Toast.LENGTH_SHORT).show();
            Log.e(APPTAG, "Corrupted item: " + (entry != null ? entry.getLinkToDetails() : null));
            if(entry instanceof DetailedEntry) StateManager.getState(parent.getContext()).history().delete(((DetailedEntry) entry), null);
            return new Space(parent.getContext());
        }
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

    public final Serializable getDataRepresentation() {
        return new EntryListAdapter.SerializableRepresentation(this.getClass(), this.entryProvider.getDataRepresentation(), serialize());
    }

    @Override
    public final boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public final boolean isEnabled(int position) {
        return true;
    }

    protected Serializable serialize() {
        return null;
    }

}
