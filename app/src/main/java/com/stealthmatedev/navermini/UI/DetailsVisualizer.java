package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsVisualizer {

    private static final String STATE_CLASS = "NM_DETAILS_STATE_CLASS";
    protected static final String STATE_DATA = "NM_DETAILS_STATE_DATA";

    public static final String KEY_DETAILS = "nm_details";

    public static DetailsVisualizer fromSavedState(Context context, Bundle savedState) {

        if(savedState == null) return null;

        String classname = savedState.getString(STATE_CLASS);
        if(classname == null) return null;

        try {
            Class<?> adapterClass = Class.forName(classname);
            Serializable data = savedState.getSerializable(STATE_DATA);
            return (DetailsVisualizer) adapterClass.getConstructor(Context.class, Serializable.class).newInstance(context, data);
        } catch (ClassNotFoundException e) {
            Log.e(APPTAG, "Could not find class " + classname + " from saved state!");
            return null;
        } catch (NoSuchMethodException e) {
            Log.e(APPTAG, "Could not find constructor in class " + classname + " from saved state!");
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

    protected final Context context;

    public DetailsVisualizer(Context context) {
        this.context = context;
    }

    public abstract View getView(ViewGroup container);

    public abstract Serializable getDataRepresentation();

    public final void saveState(Bundle outState) {
        outState.putString(STATE_CLASS, this.getClass().getCanonicalName());
        outState.putSerializable(STATE_DATA, getDataRepresentation());
    }
}
