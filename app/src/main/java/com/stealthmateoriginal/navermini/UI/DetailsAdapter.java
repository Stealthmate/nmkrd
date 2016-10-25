package com.stealthmateoriginal.navermini.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;
import com.stealthmateoriginal.navermini.state.StateManager;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsAdapter {

    public static final String KEY_DETAILS = "nm_details";

    public static DetailsAdapter fromSavedState(Bundle savedState) {



        return null;
    }

    protected final DetailsFragment fragment;

    public DetailsAdapter(DetailsFragment fragment) {
        this.fragment = fragment;
    }

    public abstract View getView(ViewGroup container);

    public abstract void save(Bundle outState);
}
