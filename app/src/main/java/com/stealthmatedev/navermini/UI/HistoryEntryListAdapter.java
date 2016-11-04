package com.stealthmatedev.navermini.UI;

import android.util.Log;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.StateManager;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/04 0004.
 */

public class HistoryEntryListAdapter extends EntryListAdapter {

    private StateManager state;

    public HistoryEntryListAdapter(StateManager state, EntryProvider entryProvider) {
        super(entryProvider);
        this.state = state;
    }

    @Override
    public Object getItem(int position) {
        long t1 = System.currentTimeMillis();
        getEntryProvider().getEntry(position);
        Log.d(APPTAG, "Count took " + (System.currentTimeMillis() - t1));
        return getEntryProvider().getEntry(position);
    }

    @Override
    protected boolean onClick(int position) {
        final Entry item = (Entry) getItem(position);
        if (item instanceof DetailedEntry) state.openDetails((DetailedEntry) item, false);
        return true;
    }
}
