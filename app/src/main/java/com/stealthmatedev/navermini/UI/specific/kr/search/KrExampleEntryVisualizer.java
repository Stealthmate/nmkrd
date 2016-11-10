package com.stealthmatedev.navermini.UI.specific.kr.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrExample;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class KrExampleEntryVisualizer extends EntryVisualizer {
    @Override
    public View visualize(Entry entry, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_text_large, parent, false);

        TextView tv = (TextView) view;

        KrExample ex = (KrExample) entry;

        if (ex == null) return tv;

        tv.setText(ex.getEx());

        return tv;
    }
}
