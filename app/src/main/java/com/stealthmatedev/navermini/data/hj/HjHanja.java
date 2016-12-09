package com.stealthmatedev.navermini.data.hj;

import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.serverapi.DetailsDictionary;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/28 0028.
 */

public class HjHanja implements DetailedEntry {

    public static class RelatedHanja implements Serializable {
        public final ArrayList<String> relShape;
        public final ArrayList<String> relMean;
        public final ArrayList<String> oppMean;

        public RelatedHanja() {
            relShape = new ArrayList<>(0);
            relMean = new ArrayList<>(0);
            oppMean = new ArrayList<>(0);
        }
    }

    public static class HanjaWord implements Serializable {

        public final String hj;
        public final String hg;

        public HanjaWord() {
            hj = "";
            hg = "";
        }

    }

    public final String hanja;
    public final ArrayList<String> readings;
    public final Character radical;
    public final int strokes;
    public final ArrayList<String> saseongeum;
    public final String difficulty;
    public final ArrayList<String> meanings;
    public final ArrayList<String> expl;
    public final String glyphexpl;
    public final RelatedHanja relHanja;
    public final ArrayList<String> strokeDiagram;
    public final ArrayList<HanjaWord> relWords;
    public final ArrayList<HanjaWord> relIdioms;
    public final boolean partial;


    public HjHanja() {

        hanja = "";
        readings = new ArrayList<>(0);
        radical = '\0';
        strokes = 0;
        saseongeum = new ArrayList<>(0);
        difficulty = "";
        meanings = new ArrayList<>(0);
        expl = new ArrayList<>(0);
        glyphexpl = "";
        relHanja = new RelatedHanja();
        strokeDiagram = new ArrayList<>(0);
        relWords = new ArrayList<>(0);
        relIdioms = new ArrayList<>(0);
        partial = true;
    }

    @Override
    public boolean isPartial() {
        return this.partial;
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.HANJA_DETAILS.path + "?q=" + URLEncoder.encode(hanja, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getRawLink() {
        return getLinkToDetails();
    }
}
