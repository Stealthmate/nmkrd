package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsAdapter {

    private static final String STATE_CLASS = "NM_STATE_CLASS";
    protected static final String STATE_DATA = "NM_STATE_DATA";

    public static final String KEY_DETAILS = "nm_details";

    public static DetailsAdapter fromSavedState(Context context, Bundle savedState) {

        if(savedState == null) return null;

        System.out.println("Recreating adapter from saved state");

        String classname = savedState.getString(STATE_CLASS);
        if(classname == null) return null;

        System.out.println("Adapter is " + classname);

        try {
            Class<?> adapterClass = Class.forName(classname);
            Serializable data = savedState.getSerializable(STATE_DATA);
            return (DetailsAdapter) adapterClass.getConstructor(Context.class, Serializable.class).newInstance(context, data);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find class " + classname + " from saved state!");
            return null;
        } catch (NoSuchMethodException e) {
            System.err.println("Could not find constructor in class " + classname + " from saved state!");
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

    public DetailsAdapter(Context context) {
        this.context = context;
    }

    public abstract View getView(ViewGroup container);

    public abstract Serializable getDataRepresentation();

    public final void saveState(Bundle outState) {
        outState.putString(STATE_CLASS, this.getClass().getCanonicalName());
        outState.putSerializable(STATE_DATA, getDataRepresentation());
    }
}
