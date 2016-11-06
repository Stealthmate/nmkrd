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
        return getEntryProvider().getEntry(position);
    }

    @Override
    public boolean onItemClicked(int position) {
        final Entry item = (Entry) getItem(position);
        if (item instanceof DetailedEntry) state.openDetails((DetailedEntry) item, false);
        return true;
    }
}
