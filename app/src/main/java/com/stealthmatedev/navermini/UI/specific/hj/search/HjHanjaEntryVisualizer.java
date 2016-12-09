package com.stealthmatedev.navermini.UI.specific.hj.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.hj.HjHanja;

/**
 * Created by Stealthmate on 16/12/08 0008.
 */

public class HjHanjaEntryVisualizer extends EntryVisualizer {
    @Override
    public View visualize(Entry entry, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hj_hanja, parent, false);

        HjHanja hanja = (HjHanja) entry;

        TextView ji = (TextView) view.findViewById(R.id.view_hj_entry_hanja);
        ji.setText(hanja.hanja.toString());

        TextView radical = (TextView) view.findViewById(R.id.view_hj_entry_hanja_radical);
        radical.setText(hanja.radical.toString());

        TextView strokes = (TextView) view.findViewById(R.id.view_hj_hanja_strokes);
        strokes.setText("" + hanja.strokes);

        TextView readings = (TextView) view.findViewById(R.id.view_hj_entry_hanja_readings);
        String read = "";
        if (hanja.readings.size() > 0) read = hanja.readings.get(0);
        for (int i = 1; i <= hanja.readings.size() - 1; i++) {
            read += "  " + hanja.readings.get(i);
        }
        readings.setText(read);

        TextView meanings = (TextView) view.findViewById(R.id.view_hj_entry_hanja_meaning);
        String mean = hanja.meanings.get(0);
        if(hanja.meanings.size() > 1) mean = "1. " + mean + "\n2. " + hanja.meanings.get(1);
        meanings.setText(mean);

        TextView difficulty = (TextView) view.findViewById(R.id.view_hj_entry_hanja_difficulty);
        difficulty.setText(hanja.difficulty);
        return view;
    }
}
