package com.stealthmatedev.navermini.UI.specific;

import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.UI.generic.ViewStyler;
import com.stealthmatedev.navermini.data.Entry;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class EntryVisualizer {

    private ViewStyler styler;

    public final void setStyler(ViewStyler styler) {
        this.styler = styler;
    }

    public final ViewStyler getStyler() {
        return this.styler;
    }

    public abstract View visualize(Entry entry, ViewGroup parent);

}
