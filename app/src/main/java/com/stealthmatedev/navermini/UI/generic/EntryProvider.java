package com.stealthmatedev.navermini.UI.generic;

import android.database.DataSetObserver;

import com.stealthmatedev.navermini.data.Entry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class EntryProvider {

    private ArrayList<DataSetObserver> observers;

    public EntryProvider() {
        this.observers = new ArrayList<>();
    }

    public final void registerDataSetObserver(DataSetObserver observer) {
        this.observers.add(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer) {
        this.observers.remove(observer);
    }

    public final void notifyDataSetChanged() {
        for(DataSetObserver observer : observers) {
            observer.onChanged();
        }
    }

    public final void notifyDataSetInvalidated() {
        for(DataSetObserver observer : observers) {
            observer.onInvalidated();
        }
    }

    public abstract Entry getEntry(int position);
    public abstract int getCount();
}
