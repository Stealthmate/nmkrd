package com.stealthmatedev.navermini.state;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/10/29 0029.
 */

public class ResultListQuery implements Serializable {

    public final String path;
    public final String query;
    public final int page;
    public final int pagesize;

    public ResultListQuery(String path, String query, int page, int pagesize) {
        this.path = path;
        this.query = query;
        this.page = page;
        this.pagesize = pagesize;
    }
}
