package com.stealthmatedev.navermini.UI.generic;

import com.stealthmatedev.navermini.data.Entry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class ArrayListEntryProvider extends EntryProvider {

    private ArrayList<Entry> entries;

    public ArrayListEntryProvider(ArrayList<Entry> entries) {
        this.entries = entries;
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
}
