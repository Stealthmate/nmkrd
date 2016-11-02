package com.stealthmatedev.navermini.state;

import android.content.Context;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.ResponseTranslator;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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

    /*public void loadDetailsFromItem(DetailedItem item, final DetailsVisualizer visualizer) {
        final DetailsFragment dfrag = openDetailsPage();

        if (item.hasDetails()) {
            loadDetailsFromURL(item.getLinkToDetails(), visualizer, dfrag);
        } else {
            visualizer.populate(item);
            dfrag.populate(visualizer);
        }
    }*/

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
                visualizer.populate(new DetailedItem.Translator().translate(response));
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
