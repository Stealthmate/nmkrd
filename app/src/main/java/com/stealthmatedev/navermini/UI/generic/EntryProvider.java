package com.stealthmatedev.navermini.UI.generic;

import android.database.DataSetObserver;

import com.stealthmatedev.navermini.data.Entry;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class EntryProvider {

    public static class EntryProviderRepresentation implements Serializable {
        private final Class<? extends EntryProvider> type;
        private final Serializable data;

        public EntryProviderRepresentation(Class<? extends EntryProvider> type, Serializable data) {
            this.type = type;
            this.data = data;
        }
    }

    public static EntryProvider deserialize(EntryProviderRepresentation repr) {
        Class<? extends EntryProvider> type = repr.type;
        try {
            return type.getConstructor(Serializable.class).newInstance(repr.data);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

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

    protected abstract Serializable serialize();

    public final Serializable getDataRepresentation() {
        return new EntryProviderRepresentation(this.getClass(), serialize());
    }
}
