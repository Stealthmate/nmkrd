package com.stealthmatedev.navermini.data;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class SentenceEntry implements Entry {

    public enum Language {
        KR("KR"),
        JP("JP"),
        EN("EN");

        public final String code;

        Language(String code) {
            this.code = code;
        }
    }

    public final Language from;
    public final Language to;

    public final String keyword;
    public final String ex;
    public final String tr;

    public SentenceEntry() {
        this.from = null;
        this.to = null;

        this.keyword = "";
        this.ex = "";
        this.tr = "";
    }

    public SentenceEntry(Language from, Language to, String keyword, String ex, String tr) {
        this.from = from;
        this.to = to;
        this.keyword = keyword;
        this.ex = ex;
        this.tr = tr;
    }


    @Override
    public boolean isPartial() {
        return false;
    }

    @Override
    public String getLinkToDetails() {
        return null;
    }

    @Override
    public String getRawLink() {
        return null;
    }
}
