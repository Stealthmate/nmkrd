package com.stealthmatedev.navermini.state;

import android.content.Context;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.data.CallbackAsyncTask;
import com.stealthmatedev.navermini.data.DBHelper;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryEntry;
import com.stealthmatedev.navermini.data.history.HistoryTableManager;
import com.stealthmatedev.navermini.data.sentencestore.SentenceStoreTableManager;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class StateManager {

    private static StateManager state;

    public static StateManager createState(MainActivity activity) {
        if(state != null) {
            throw new RuntimeException("State has already been initialized!");
        }

        state = new StateManager(activity);
        return state;
    }

    private MainActivity activity;
    private SearchEngine searchEngine;
    private DBHelper dbHelper;

    private StateManager(MainActivity activity) {
        this.activity = activity;
        this.searchEngine = new SearchEngine(this);
        this.dbHelper = new DBHelper(activity);
    }

    public SentenceStoreTableManager sentenceStore() {
        return dbHelper.sentenceStore();
    }

    public DBHelper dbhelper() {
        return dbHelper;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public HistoryTableManager history() {
        return dbHelper.history();
    }

    public void openDetails(final DetailedEntry entry, final boolean save) {
        final DetailsFragment dfrag = openDetailsPage(entry);
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
        dfrag.setVisualizer(finalVisualizer);

        dfrag.waitForData();
        history().findById(new HistoryEntry(entry).getId(), new CallbackAsyncTask.Callback() {
            @Override
            public void callback(Object historyEntry) {

                DetailedEntry actualEntry = entry;

                if (historyEntry != null) {
                    actualEntry = (DetailedEntry) historyEntry;
                }

                if (actualEntry.isPartial()) {
                    final DetailedEntry finalEntry = actualEntry;
                    getSearchEngine().queryDetails(actualEntry.getLinkToDetails(), new SearchEngine.OnResponse() {
                        @Override
                        public void responseReady(String response) {
                            DetailedEntry detailedEntry = (DetailedEntry) new Entry.Translator(finalEntry.getClass()).translate(response);
                            if(save) history().put(detailedEntry, null);
                            finalVisualizer.populate(detailedEntry);
                        }

                        @Override
                        public void onError(VolleyError err) {
                            closePage(dfrag);
                        }

                        @Override
                        public void onRedirect() {
                            closePage(dfrag);
                        }
                    });
                } else {
                    finalVisualizer.populate(actualEntry);
                    if(save) history().put(actualEntry, null);
                }
            }
        });
    }

    public DetailsFragment openDetailsPage(DetailedEntry entry) {
        DetailsFragment dfrag = activity.openNewDetailsPage(entry);
        return dfrag;
    }

    public void closePage(DetailsFragment frag) {
        if (frag.isAdded()) activity.onBackPressed();
    }

    public static StateManager getState(Context context) {
        return ((MainActivity) context).getState();
    }

    public static StateManager getState() {
        return state;
    }

}
