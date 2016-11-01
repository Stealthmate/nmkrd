package com.stealthmatedev.navermini.data.kr;

import android.content.Context;
import android.util.Log;

import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.state.DetailedItem;

import static com.stealthmatedev.navermini.App.APPTAG;

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

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KrExampleEntry && text.equals(((KrExampleEntry) obj).text);
    }
}
