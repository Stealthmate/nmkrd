package com.stealthmatedev.navermini.data.en;

import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.state.DetailsDictionary;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthma 0030.
 */

public class EnWord implements DetailedItem, Serializable {

    public static class WordClassGroup implements Serializable {
        public static class Meaning implements Serializable {
            public final String m;
            public final String enWord;
            public final ArrayList<TranslatedExample> ex;

            public Meaning() {
                this.m = "";
                this.enWord = "";
                this.ex = new ArrayList<>();
            }
        }

        public final String wclass;
        public final ArrayList<Meaning> meanings;

        public WordClassGroup() {
            this.wclass = "";
            this.meanings = new ArrayList<>();
        }
    }

    public final String word;
    public final String hanja;
    public final String pronun;
    public final String wclass;
    public final ArrayList<WordClassGroup> clsgrps;
    public final String more;

    public EnWord() {
        this.word = "";
        this.hanja = "";
        this.pronun = "";
        this.wclass = "";
        this.more = "";
        this.clsgrps = new ArrayList<>();
    }

    @Override
    public boolean hasDetails() {
        return true;
    }

    @Override
    public String getLinkToDetails() {
        if (more.startsWith("http")) return more;

        try {
            return DetailsDictionary.ENGLISH_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(more + "&sLn=en", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRawLink() {
        return this.more;
    }

}
