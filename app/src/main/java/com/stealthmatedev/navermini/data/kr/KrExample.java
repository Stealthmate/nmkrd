package com.stealthmatedev.navermini.data.kr;

import com.stealthmatedev.navermini.data.Entry;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExample implements Entry {

    private final String text;

    public KrExample(String text) {
        this.text = text;
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
        return text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KrExample && text.equals(((KrExample) obj).text);
    }
}
