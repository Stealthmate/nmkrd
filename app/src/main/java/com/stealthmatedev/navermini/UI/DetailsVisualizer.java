package com.stealthmatedev.navermini.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.data.DetailedEntry;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsVisualizer {

    public interface Observer {
        void onPopulated();
    }

    private static final String STATE_CLASS = "NM_DETAILS_STATE_CLASS";
    private static final String STATE_DATA = "NM_DETAILS_STATE_DATA";

    public static DetailsVisualizer fromSavedState(Bundle savedState) {

        if (savedState == null) return null;

        String classname = savedState.getString(STATE_CLASS);

        if (classname == null) return null;

        try {
            @SuppressWarnings("unchecked") Class<? extends DetailsVisualizer> adapterClass = (Class<? extends DetailsVisualizer>) Class.forName(classname);
            DetailedEntry data = (DetailedEntry) savedState.getSerializable(STATE_DATA);
            DetailsVisualizer visualizer = adapterClass.newInstance();
            visualizer.populate(data);
            return visualizer;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private DetailedEntry details;
    private ArrayList<Observer> observers;

    public DetailsVisualizer() {
        details = null;
        observers = new ArrayList<>();
    }

    public final DetailedEntry getDetails() {
        return details;
    }

    public final void populate(DetailedEntry details) {
        this.details = details;
        notifyObservers();
    }

    public final void saveState(Bundle outState) {
        outState.putString(STATE_CLASS, this.getClass().getCanonicalName());
        outState.putSerializable(STATE_DATA, details);
    }

    public abstract View getView(ViewGroup container);

    public void registerObserver(Observer obs) {
        observers.add(obs);
    }

    public void unregisterObserver(Observer obs) {
        observers.remove(obs);
    }

    private void notifyObservers() {
        for (Observer obs : observers) obs.onPopulated();
    }
}
