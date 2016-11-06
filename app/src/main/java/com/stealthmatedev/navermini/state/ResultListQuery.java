package com.stealthmatedev.navermini.state;

import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/10/29 0029.
 */

public class ResultListQuery implements Serializable {

    public final String path;
    public final String query;
    public final int page;
    public final int pagesize;
    private final String dict_name;
    private final int subdict_index;

    public ResultListQuery(String path, String query, int page, int pagesize, EntryListDictionary.SubDictionary dict) {
        this.path = path;
        this.query = query;
        this.page = page;
        this.pagesize = pagesize;
        this.dict_name = dict.parent;
        this.subdict_index = EntryListDictionary.valueOf(dict.parent).indexOf(dict);
    }

    public EntryListDictionary getDictionary() {
        return EntryListDictionary.valueOf(dict_name);
    }

    public EntryListDictionary.SubDictionary getSubDictionary() {
        return getDictionary().subdicts[subdict_index];
    }
}
