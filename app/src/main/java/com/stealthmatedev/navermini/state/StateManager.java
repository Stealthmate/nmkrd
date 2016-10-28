package com.stealthmatedev.navermini.state;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.App;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.fragments.SearchFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class StateManager {

    private static final String HOST = App.HOST;
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PAGESIZE = "pagesize";

    private MainActivity activity;
    private SearchFragment searchFragment;
    private DetailsFragment detailsFragment;
    private SearchEngine searchEngine;

    private ResultListDictionary currentDictionary;

    private ResultListDictionary.SubDictionary currentSubDictionary;

    public StateManager(MainActivity activity) {
        this.activity = activity;
        this.searchFragment = new SearchFragment();
        this.detailsFragment = new DetailsFragment();
        this.searchEngine = new SearchEngine(this);
        this.currentDictionary = ResultListDictionary.KOREAN;
        this.currentSubDictionary = currentDictionary.subdicts[0];
    }

    public MainActivity getActivity() {
        return activity;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }
    public void setCurrentDictionary(ResultListDictionary dict) {

        this.currentDictionary = dict;
        setCurrentSubDictionary(0);
    }

    public ResultListDictionary getCurrentDictionary() {
        return this.currentDictionary;
    }

    public ResultListDictionary.SubDictionary getCurrentSubDictionary() {
        return currentSubDictionary;
    }

    public void setCurrentSubDictionary(int i) {
        this.searchEngine.cancellAllQueries();
        this.currentSubDictionary = currentDictionary.subdicts[i];
        this.searchFragment.performSearch();
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public void query(String query, int page, int pagesize, SearchEngine.OnResponse callback) {

        String url = HOST;
        url += currentDictionary.path + currentSubDictionary.path;
        url += "?" + PARAM_PAGE + "=" + page;
        url += "&" + PARAM_PAGESIZE + "=" +  pagesize;
        try {
            url += "&" + PARAM_QUERY + "=" +  URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(APPTAG, "Unsupported utf-8.");
            activity.reset();
        }

        searchEngine.request(url, callback);
    }

    public void loadDetails(final DetailedItem obj) {
        if(!obj.hasDetails()) return;

        String link = obj.getLinkToDetails();
        System.out.println(link);

        if(link.startsWith("http")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            activity.startActivity(browserIntent);
            return;
        }

        final DetailsFragment dfrag = new DetailsFragment();
        searchEngine.request(HOST + obj.getLinkToDetails(), new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                dfrag.populate(obj.createAdapterFromDetails(activity, response));
            }
        });
        activity.openNewDetailsPage(dfrag);
        dfrag.waitForData();
    }

    public static StateManager getState(Context context) {
        return ((MainActivity)context).getState();
    }
}
