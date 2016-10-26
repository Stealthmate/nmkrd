package com.stealthmateoriginal.navermini.data.kr;

import android.content.Context;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.state.DetailedItem;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExampleEntry implements DetailedItem {

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
    public DetailsAdapter createAdapterFromDetails(Context context, String details) {
        return null;
    }

    public String getText() {
        return text;
    }
}
