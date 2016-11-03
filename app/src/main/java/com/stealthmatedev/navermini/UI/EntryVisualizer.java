package com.stealthmatedev.navermini.UI;

import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.data.Entry;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public interface EntryVisualizer {
    View visualize(Entry entry, ViewGroup parent);
}
