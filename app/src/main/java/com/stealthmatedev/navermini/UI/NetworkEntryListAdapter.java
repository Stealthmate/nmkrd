package com.stealthmatedev.navermini.UI;

import android.util.Log;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.generic.ArrayListEntryProvider;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryEntry;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

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
    protected boolean onClick(int position) {
        final Entry item = (Entry) getItem(position);
        if(item instanceof DetailedEntry) state.openDetails((DetailedEntry) item);
        return true;
    }
}
