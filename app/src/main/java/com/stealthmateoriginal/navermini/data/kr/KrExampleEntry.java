package com.stealthmateoriginal.navermini.data.kr;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.state.ResultListItem;
import com.stealthmateoriginal.navermini.state.StateManager;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExampleEntry implements ResultListItem {

    private final String text;

    public KrExampleEntry(String text) {
        this.text = text;
    }


    @Override
    public boolean hasDetails() {
        return false;
    }

    @Override
    public String getLinkToDetails() {
        return null;
    }

    @Override
    public DetailsAdapter createAdapterFromDetails(StateManager state, String details) {
        return null;
    }

    public String getText() {
        return text;
    }
}
