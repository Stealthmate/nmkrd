package com.stealthmatedev.navermini.UI;

import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.Entry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public abstract class UIEntry {

    protected final Entry data;

    public UIEntry(Entry data) {
        this.data = data;
    }

    public void highlight(String str) {

    }

    public void onClick() {

        

    }

    public abstract View getView(ViewGroup parent);
}
