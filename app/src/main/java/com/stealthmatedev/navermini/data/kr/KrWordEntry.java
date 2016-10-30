package com.stealthmatedev.navermini.data.kr;

import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.DetailsDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class KrWordEntry implements DetailedItem {

    private static final String NAME = "word";
    private static final String HANJA = "hanja";
    private static final String PRONUN = "pronun";
    private static final String CLASS = "class";
    private static final String MORE = "more";
    private static final String MEANING = "gloss";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    public final String word;
    public final String def;

    public final String hanja;
    public final String pronun;
    public final String[] wclass;

    public final String more;

    private KrWordEntry() {
        this.word = null;
        this.hanja = null;
        this.pronun = null;
        this.wclass = null;
        this.def = null;
        this.more = null;
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof KrWordEntry)) return false;

        KrWordEntry word = (KrWordEntry) obj;

        if (!this.word.equals(word.word)) return false;
        if (!def.equals(word.def)) return false;
        if (!hanja.equals(word.hanja)) return false;
        if (!pronun.equals(word.pronun)) return false;

        if (!more.equals(word.more)) return false;

        if (wclass.length != word.wclass.length) return false;

        for (int i = 0; i <= wclass.length - 1; i++) {
            if(!wclass[i].equals(word.wclass[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return word + " " + hanja + " " + pronun + " " + def;
    }

    @Override
    public boolean hasDetails() {
        return more != null && !more.equals(NO_MORE_INFO);
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.KOREAN_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(more, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
