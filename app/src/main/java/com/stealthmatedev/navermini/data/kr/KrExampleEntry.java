package com.stealthmatedev.navermini.data.kr;

import com.stealthmatedev.navermini.data.DetailedItem;

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
