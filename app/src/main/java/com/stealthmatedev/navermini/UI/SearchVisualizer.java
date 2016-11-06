package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public abstract class SearchVisualizer {

    private static final String STATE_CLASS = "NM_SEARCH_STATE_CLASS";
    private static final String STATE_DATA = "NM_SEARCH_STATE_DATA";

    public static NetworkEntryListAdapter fromSavedState(StateManager state, Bundle savedState) {

        if (savedState == null) return null;

        String classname = savedState.getString(STATE_CLASS);
        if (classname == null) return null;

        try {
            Class<?> adapterClass = Class.forName(classname);
            Serializable data = savedState.getSerializable(STATE_DATA);
            //return (SearchVisualizer) adapterClass.getConstructor(StateManager.class, Serializable.class).newInstance(state, data);
            return (NetworkEntryListAdapter) EntryListAdapter.deserialize(state, (EntryListAdapter.SerializableRepresentation) data);
        } catch (ClassNotFoundException e) {
            Log.e(APPTAG, "Could not find class " + classname + " from saved state!");
            return null;
        }
    }

    private Context context;

    public SearchVisualizer(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @NonNull
    public abstract View getView(@NonNull ViewGroup container);

    public abstract Serializable getDataRepresentation();

    public final void saveState(Bundle outState) {
        outState.putString(STATE_CLASS, this.getClass().getCanonicalName());
        outState.putSerializable(STATE_DATA, getDataRepresentation());
    }
}
