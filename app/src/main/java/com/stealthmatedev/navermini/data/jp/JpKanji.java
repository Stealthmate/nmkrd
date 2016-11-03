package com.stealthmatedev.navermini.data.jp;

import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.serverapi.DetailsDictionary;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/31 0031.
 */

public class JpKanji implements DetailedEntry {

    public static class Meaning implements Serializable {
        public final String m;
        public final ArrayList<TranslatedExample> ex;

        public Meaning() {
            this.m = "";
            this.ex = new ArrayList<>();
        }
    }

    public static class WordLinkPair implements Serializable {
        public final String word;
        public final String lnk;

        public WordLinkPair() {
            this.word = "";
            this.lnk = "";
        }
    }

    public final Character kanji;
    public final ArrayList<String> kunyomi;
    public final ArrayList<String> onyomi;
    public final ArrayList<String> kr;
    public final int strokes;
    public final Character radical;
    public final ArrayList<Meaning> meanings;
    public final ArrayList<WordLinkPair> kunex;
    public final ArrayList<WordLinkPair> onex;
    public final String more;

    public JpKanji() {
        this.kanji = '\0';

        this.kunyomi = new ArrayList<>();
        this.onyomi = new ArrayList<>();
        this.kr = new ArrayList<>();

        this.strokes = 0;
        this.radical = '\0';

        this.meanings = new ArrayList<>();
        this.kunex = new ArrayList<>();
        this.onex = new ArrayList<>();

        this.more = "";
    }


    @Override
    public boolean isPartial() {
        return more.length() > 0;
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.JAPANESE_DETAILS.path + "?lnk=" + URLEncoder.encode(more, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getRawLink() {
        return more;
    }
}
