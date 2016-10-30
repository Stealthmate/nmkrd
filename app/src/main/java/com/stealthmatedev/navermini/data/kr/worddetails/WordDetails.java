package com.stealthmatedev.navermini.data.kr.worddetails;

import com.stealthmatedev.navermini.data.kr.KrWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class WordDetails implements Serializable {

    public final Word wordinfo;
    public final ArrayList<Definition> defs;

    public WordDetails(Word wordinfo, ArrayList<Definition> defs) {
        this.wordinfo = wordinfo;
        this.defs = defs;
    }

    public WordDetails(String json) {
        wordinfo = null;
        defs = null;
    }

    public WordDetails(KrWordEntry wordinfo) {
        this.wordinfo = new Word(wordinfo.word, wordinfo.pronun, wordinfo.hanja, wordinfo.wclass);
        this.defs = new ArrayList<>(1);
        this.defs.add(new Definition(wordinfo.def, null));
    }
}
