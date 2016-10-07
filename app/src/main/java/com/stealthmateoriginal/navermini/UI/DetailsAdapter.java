package com.stealthmateoriginal.navermini.UI;

import android.view.View;

import com.stealthmateoriginal.navermini.state.StateManager;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class DetailsAdapter {

    protected final StateManager state;
    protected final String response;

    public DetailsAdapter(StateManager state, String response) {
        this.state = state;
        this.response = response;
    }

    public abstract void populateContainer(View container);
}
