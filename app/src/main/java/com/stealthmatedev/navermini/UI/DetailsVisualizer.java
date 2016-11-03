package com.stealthmatedev.navermini.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.data.DetailedEntry;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsVisualizer {

    private static final String STATE_CLASS = "NM_DETAILS_STATE_CLASS";
    private static final String STATE_DATA = "NM_DETAILS_STATE_DATA";

    public static DetailsVisualizer fromSavedState(Bundle savedState) {

        if (savedState == null) return null;

        String classname = savedState.getString(STATE_CLASS);
        Log.d(APPTAG, "Creating details visualizer from saved state for " + classname);
        if (classname == null) return null;

        try {
            @SuppressWarnings("unchecked") Class<? extends DetailsVisualizer> adapterClass = (Class<? extends DetailsVisualizer>) Class.forName(classname);
            DetailedEntry data = (DetailedEntry) savedState.getSerializable(STATE_DATA);
            DetailsVisualizer visualizer = adapterClass.newInstance();
            visualizer.populate(data);
            Log.d(APPTAG, "Creating details visualizer from saved state for " + classname);
            return visualizer;
        } catch (ClassNotFoundException e) {
            Log.d(APPTAG, "Could not find class " + classname + " from saved state!");
            return null;
        } catch (IllegalAccessException e) {
            Log.d(APPTAG, "", e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            Log.d(APPTAG, "", e);
            e.printStackTrace();
        }

        return null;
    }

    private DetailedEntry details;

    public DetailsVisualizer() {
        details = null;
    }

    public final DetailedEntry getDetails() {
        return details;
    }

    public final void populate(DetailedEntry details) {
        this.details = details;
    }

    public final void saveState(Bundle outState) {
        outState.putString(STATE_CLASS, this.getClass().getCanonicalName());
        outState.putSerializable(STATE_DATA, details);
    }

    public abstract View getView(ViewGroup container);
}
