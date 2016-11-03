package com.stealthmatedev.navermini.state;

import android.content.Context;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.specific.EntryUIMapper;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class StateManager {

    private MainActivity activity;
    private SearchEngine searchEngine;

    public StateManager(MainActivity activity) {
        this.activity = activity;
        this.searchEngine = new SearchEngine(this);
    }

    public MainActivity getActivity() {
        return activity;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    //public void saveToHistory(String json, )

    /*public void loadDetailsFromItem(Entry item, final DetailsVisualizer visualizer) {
        final DetailsFragment dfrag = openDetailsPage();

        if (item.isPartial()) {
            loadDetailsFromURL(item.getLinkToDetails(), visualizer, dfrag);
        } else {
            visualizer.populate(item);
            dfrag.populate(visualizer);
        }
    }*/

    public void openDetails(final DetailedEntry entry) {
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

        if (entry.isPartial()) {
            dfrag.waitForData();
            final DetailsVisualizer finalVisualizer = visualizer;
            getSearchEngine().queryDetails(entry.getLinkToDetails(), new SearchEngine.OnResponse() {
                @Override
                public void responseReady(String response) {
                    finalVisualizer.populate((DetailedEntry) new Entry.Translator(entry.getClass()).translate(response));
                    dfrag.populate(finalVisualizer);
                }

                @Override
                public void onError(VolleyError err) {
                    closePage(dfrag);
                }
            });
        } else {
            dfrag.populate(visualizer);
        }
    }

    public DetailsFragment openDetailsPage() {
        DetailsFragment dfrag = new DetailsFragment();
        activity.openNewDetailsPage(dfrag);
        return dfrag;
    }

    public void closePage(DetailsFragment frag) {
        if(frag.isAdded()) activity.onBackPressed();
    }

    /*public void loadDetailsFromURL(String path, final DetailsVisualizer visualizer, DetailsFragment frag) {
        if(frag == null) frag = openDetailsPage();
        frag.waitForData();
        final DetailsFragment finalFrag = frag;

        getSearchEngine().queryDetails(path, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                visualizer.populate(new Entry.Translator().translate(response));
                finalFrag.populate(visualizer);
            }

            @Override
            public void onError(VolleyError err) {
                if(finalFrag.isAdded()) activity.onBackPressed();
            }
        });
    }*/

    public static StateManager getState(Context context) {
        return ((MainActivity)context).getState();
    }


}
