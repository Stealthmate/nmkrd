package com.stealthmatedev.navermini.state;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryManager;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class StateManager {

    private MainActivity activity;
    private SearchEngine searchEngine;
    private HistoryManager history;

    public StateManager(MainActivity activity) {
        this.activity = activity;
        this.searchEngine = new SearchEngine(this);
        this.history = new HistoryManager(activity);
    }

    public MainActivity getActivity() {
        return activity;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public HistoryManager history() {
        return this.history;
    }

    public void openDetails(final DetailedEntry entry, final boolean save) {
        final DetailsFragment dfrag = openDetailsPage();
        DetailsVisualizer visualizer = null;
        try {
            visualizer = EntryUIMapper.forEntry(entry).detailsVisualizerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        visualizer.populate(entry);

        final DetailsVisualizer finalVisualizer = visualizer;
        dfrag.populate(finalVisualizer);

        history().get(entry, new HistoryManager.Callback() {
            @Override
            public void onFinish(Object historyEntry) {

                DetailedEntry actualEntry = entry;

                if (historyEntry != null) {
                    actualEntry = (DetailedEntry) historyEntry;
                }

                finalVisualizer.populate(actualEntry);

                if (actualEntry.isPartial()) {
                    final DetailedEntry finalEntry = actualEntry;
                    dfrag.waitForData();
                    getSearchEngine().queryDetails(actualEntry.getLinkToDetails(), new SearchEngine.OnResponse() {
                        @Override
                        public void responseReady(String response) {
                            DetailedEntry detailedEntry = (DetailedEntry) new Entry.Translator(finalEntry.getClass()).translate(response);
                            if(save) history.save(detailedEntry);
                            finalVisualizer.populate(detailedEntry);
                            dfrag.populate(finalVisualizer);
                        }

                        @Override
                        public void onError(VolleyError err) {
                            closePage(dfrag);
                        }
                    });
                } else if(save) history.save(actualEntry);
            }
        });
    }

    public DetailsFragment openDetailsPage() {
        DetailsFragment dfrag = new DetailsFragment();
        activity.openNewDetailsPage(dfrag);
        return dfrag;
    }

    public void closePage(DetailsFragment frag) {
        if (frag.isAdded()) activity.onBackPressed();
    }

    public static StateManager getState(Context context) {
        return ((MainActivity) context).getState();
    }


}
