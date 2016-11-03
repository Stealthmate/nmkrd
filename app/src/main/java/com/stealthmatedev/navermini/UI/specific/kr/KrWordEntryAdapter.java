package com.stealthmatedev.navermini.UI.specific.kr;

import com.stealthmatedev.navermini.UI.EntryAdapter;
import com.stealthmatedev.navermini.UI.StyledEntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.state.ResultListQuery;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class KrWordEntryAdapter implements EntryAdapter {
    @Override
    public StyledEntryVisualizer getVisualizerFor(Entry entry, ResultListQuery query) {
        if(!(entry instanceof KrWord)) throw new IllegalArgumentException("Cannot get visualizer for invalid item class. Required: KrWord but got " + entry.getClass());

        return new KrWordEntryVisualizer(query);
    }
}
