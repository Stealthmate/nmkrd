package com.stealthmateoriginal.navermini.state;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public abstract class SearchResults<T> {

    private String query;
    private String response;
    private int page;

    public SearchResults(String query, int page, String response) {
        this.query = query;
        this.page = page;
        this.response = response;
    }

    public abstract T getItem(int i);

    public abstract int size();

    public final String getQuery() {
        return query;
    }

    public final int getPage() {
        return page;
    }

}
