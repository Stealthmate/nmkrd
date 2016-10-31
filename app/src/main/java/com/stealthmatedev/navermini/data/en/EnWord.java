package com.stealthmatedev.navermini.data.en;

import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.state.DetailedItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthma 0030.
 */

public class EnWord implements DetailedItem, Serializable {

    public static class Meaning {
        public final String m;
        public final String enWord;
        public final ArrayList<TranslatedExample> ex;

        public Meaning() {
            this.m = "";
            this.enWord = "";
            this.ex = new ArrayList<>();
        }
    }

    public final String word;
    public final String hanja;
    public final String pronun;
    public final String wclass;
    public final ArrayList<Meaning> meanings;
    public final String more;

    public EnWord() {
        this.word = "";
        this.hanja = "";
        this.pronun = "";
        this.wclass = "";
        this.more = "";
        this.meanings = new ArrayList<>();
    }


    @Override
    public boolean hasDetails() {
        return false;
    }

    @Override
    public String getLinkToDetails() {
        return null;
    }
}
