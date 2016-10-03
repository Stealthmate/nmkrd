package com.stealthmateoriginal.navermini.UI;

import android.view.View;
import android.widget.AdapterView;

import com.stealthmateoriginal.navermini.state.StateManager;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class EngineSelectorItemSelectedListener implements AdapterView.OnItemSelectedListener {

    private StateManager state;

    public EngineSelectorItemSelectedListener(StateManager state) {
        this.state = state;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }
}
