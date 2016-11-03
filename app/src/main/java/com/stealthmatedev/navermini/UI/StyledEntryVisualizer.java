package com.stealthmatedev.navermini.UI;

import com.stealthmatedev.navermini.UI.generic.ViewStyler;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class StyledEntryVisualizer implements EntryVisualizer {

    private ViewStyler styler;

    public final void setStyler(ViewStyler styler) {
        this.styler = styler;
    }

    public final ViewStyler getStyler() {
        return this.styler;
    }

}
