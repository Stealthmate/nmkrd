package com.stealthmateoriginal.navermini.state;

import com.stealthmateoriginal.navermini.MainActivity;
import com.stealthmateoriginal.navermini.UI.DetailsFragment;
import com.stealthmateoriginal.navermini.UI.SearchFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class StateManager {

    private static final String HOST = "http://naver-mini.herokuapp.com";
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

    public DetailsFragment getDetailsFragment() {
        return detailsFragment;
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
        this.searchFragment.clear();
        this.detailsFragment.clear();
        this.activity.goToSearch();
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
            System.out.println("UNSUPPORTED ENCODING WTF EXITING");
            System.exit(-1);
            e.printStackTrace();
        }

        searchEngine.request(url, callback);
    }

    public void loadDetails(final ResultListItem obj) {
        System.out.println(HOST + obj.getLinkToDetails());
        searchEngine.request(HOST + obj.getLinkToDetails(), new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                System.out.println("RESPONSE!");
                detailsFragment.populate(obj.createAdapterFromDetails(StateManager.this, response));
            }
        });
        detailsFragment.waitForData();
        activity.goToDetails();
    }

}
