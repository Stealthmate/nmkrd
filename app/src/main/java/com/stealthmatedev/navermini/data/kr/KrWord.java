package com.stealthmatedev.navermini.data.kr;

import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.DetailsDictionary;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class KrWord implements Serializable, DetailedItem {

    public static class Definition implements Serializable {

        public final String def;
        public final ArrayList<String> ex;

        public Definition() {
            this.def = "";
            this.ex = new ArrayList<>(0);
        }

        public Definition(String def, ArrayList<String> ex) {
            this.def = def;
            if(ex != null) this.ex = ex;
            else this.ex = new ArrayList<>(0);
        }


        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Definition)) return false;

            Definition d = (Definition) obj;

            if(!def.equals(d.def)) return false;

            if (ex.size() != d.ex.size()) return false;

            for (int i = 0; i <= ex.size() - 1; i++) {
                if (!ex.get(i).equals(d.ex.get(i))) return false;
            }

            return true;
        }
    }

    public final String word;
    public final String hanja;
    public final String pronun;
    public final String wclass;
    public final String more;

    public final ArrayList<Definition> defs;

    public KrWord() {
        word = "";
        hanja = "";
        pronun = "";
        wclass = "";
        defs = new ArrayList<>(0);
        more = "";
    }

    public KrWord(String word, String hanja, String pronun, String wclass, ArrayList<Definition> defs, String more) {
        this.word = word;
        this.hanja = hanja;
        this.pronun = pronun;
        this.wclass = wclass;
        this.defs = defs;
        this.more = more;
    }

    public KrWord(KrWord wordinfo) {
        this.word = wordinfo.word;
        this.pronun = wordinfo.pronun;
        this.hanja = wordinfo.hanja;
        this.wclass = wordinfo.wclass;
        this.defs = wordinfo.defs;
        this.more = wordinfo.more;
    }

    @Override
    public boolean hasDetails() {
        return more.length() != 0;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KrWord)) return false;

        KrWord w = (KrWord) obj;

        if (!word.equals(w.word)) return false;
        if (!hanja.equals(w.hanja)) return false;
        if (!pronun.equals(w.pronun)) return false;
        if (!wclass.equals(w.wclass)) return false;
        if (!more.equals(w.more)) return false;

        if (defs.size() != w.defs.size()) return false;

        for (int i = 0; i <= defs.size() - 1; i++) {
            if (!defs.get(i).equals(w.defs.get(i))) return false;
        }

        return true;
    }
}
