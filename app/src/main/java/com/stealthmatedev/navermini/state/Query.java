package com.stealthmatedev.navermini.state;

import com.stealthmatedev.navermini.serverapi.EntryListDictionary;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */
public class Query {

    private static final String PARAM_QUERY = "q";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PAGESIZE = "pagesize";

    private String path;
    private String query;
    private int page;
    private int pagesize;

    public Query(EntryListDictionary dict) {
        this.path = dict.path;
        this.query = "";
        this.page = 0;
        this.pagesize = 0;
    }

    public String query() {
        return this.query;
    }

    public Query query(String query) {
        this.query = query;
        return this;
    }

    public int page() {
        return this.page;
    }

    public Query page(int page) {
        this.page = page;
        return this;
    }

    public int pagesize() {
        return this.pagesize;
    }

    public Query pagesize(int pagesize) {
        this.pagesize = pagesize;
        return this;
    }

    public String url() throws UnsupportedEncodingException {
        return path + "?" + PARAM_QUERY + "=" + URLEncoder.encode(query, "utf-8") + "&" + PARAM_PAGE + "=" + page + "&" + PARAM_PAGESIZE + "=" + pagesize;
    }
}
