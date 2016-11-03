package com.stealthmatedev.navermini.UI;

import android.util.Log;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.generic.ArrayListEntryProvider;
import com.stealthmatedev.navermini.data.DetailedItem;
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


    public NetworkEntryListAdapter(StateManager state, ArrayList<Entry> entries, EntryVisualizer visualizer, ResultListQuery query) {
        super(new ArrayListEntryProvider(entries), visualizer);
        this.query = query;
        this.state = state;
    }

    @Override
    protected boolean onClick(int position) {
        final DetailedItem item = (DetailedItem) getItem(position);

        if (item == null) {
            Log.e(APPTAG, "Null item in result list at position " + position);
            return false;
        }

        Log.i(APPTAG, new HistoryEntry(item).getJson());

        final DetailsFragment dfrag = state.openDetailsPage();
        final DetailsVisualizer visualizer = getDetailsVisualizer(item);
        visualizer.populate(item);

        if (item.hasDetails()) {
            dfrag.waitForData();
            state.getSearchEngine().queryDetails(item.getLinkToDetails(), new SearchEngine.OnResponse() {
                @Override
                public void responseReady(String response) {
                    visualizer.populate(new DetailedItem.Translator(item.getClass()).translate(response));
                    dfrag.populate(visualizer);
                }

                @Override
                public void onError(VolleyError err) {
                    state.closePage(dfrag);
                }
            });
        } else {
            dfrag.populate(visualizer);
        }

        return true;
    }
}
