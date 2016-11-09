package com.stealthmatedev.navermini.data.kr;

import android.support.v4.util.Pair;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.SentenceEntry;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExample extends SentenceEntry {

    public KrExample() {
        super();
    }

    @Override
    public boolean isPartial() {
        return false;
    }

    @Override
    public String getLinkToDetails() {
        return null;
    }

    @Override
    public String getRawLink() {
        return ex;
    }

    public String getEx() {
        return ex;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KrExample && ex.equals(((KrExample) obj).ex);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
