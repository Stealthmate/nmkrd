package com.stealthmatedev.navermini.UI;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.data.Entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class ArrayListEntryProvider extends EntryProvider {

    private ArrayList<Entry> entries;

    public ArrayListEntryProvider(Serializable data) {
        this.entries = (ArrayList<Entry>) data;
    }

    public ArrayListEntryProvider(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public void addAll(Collection<? extends Entry> newEntries) {
        entries.addAll(newEntries);
        notifyDataSetChanged();
    }

    public void add(Entry entry) {
        entries.add(entry);
        notifyDataSetChanged();
    }

    @Override
    public Entry getEntry(int position) {
        return entries.get(position);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    protected Serializable serialize() {
        return entries;
    }
}
