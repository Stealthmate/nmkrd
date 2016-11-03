package com.stealthmatedev.navermini.UI;

import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.ResultListQuery;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public interface EntryAdapter {

    StyledEntryVisualizer getVisualizerFor(Entry entry, ResultListQuery query);


}
