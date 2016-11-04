package com.stealthmatedev.navermini.UI;

import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class NetworkEntryListAdapter extends EntryListAdapter {

    private ResultListQuery query;
    private StateManager state;

    public NetworkEntryListAdapter(StateManager state, ArrayList<Entry> entries, ResultListQuery query) {
        super(new ArrayListEntryProvider(entries));
        this.query = query;
        this.state = state;
    }

    @Override
    public boolean onItemClicked(int position) {
        final Entry item = (Entry) getItem(position);
        if(item instanceof DetailedEntry) state.openDetails((DetailedEntry) item, true);
        return true;
    }
}
