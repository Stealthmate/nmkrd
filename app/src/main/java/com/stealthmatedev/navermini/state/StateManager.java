package com.stealthmatedev.navermini.state;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.App;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.fragments.SearchFragment;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;

import static com.stealthmatedev.navermini.App.APPTAG;

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

    public DetailsFragment openDetails() {
        DetailsFragment dfrag = new DetailsFragment();
        activity.openNewDetailsPage(dfrag);
        return dfrag;
    }

    public void loadDetailsAsync(String path, final DetailsVisualizer visualizer, DetailsFragment frag) {
        if(frag == null) frag = openDetails();
        frag.waitForData();
        final DetailsFragment finalFrag = frag;
        getSearchEngine().queryDetails(path, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                visualizer.populate(response);
                finalFrag.populate(visualizer);
            }
        });
    }

    public static StateManager getState(Context context) {
        return ((MainActivity)context).getState();
    }


}
