package com.stealthmatedev.navermini.data.jp.kanjidetails;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/21 0021.
 */

public class KanjiDetails implements Serializable {

    private static final String JSON_JI = "ji";
    private static final String JSON_KUNYOMI = "kun";
    private static final String JSON_ONYOMI = "on";
    private static final String JSON_KOREAN_READINGS = "kr";
    private static final String JSON_STROKES = "str";
    private static final String JSON_RADICAL = "rad";
    private static final String JSON_MEANINGS = "mean";
    private static final String JSON_WORDEX_WORD = "wordinfo";
    private static final String JSON_WORDEX_LNK = "lnk";
    private static final String JSON_KUNEX = "kunex";
    private static final String JSON_ONEX = "onex";

    public final Character kanji;
    public final ArrayList<String> kunyomi;
    public final ArrayList<String> onyomi;
    public final ArrayList<String> kr;
    public final int strokes;
    public final Character radical;
    public final ArrayList<KanjiMeaning> meanings;
    public final ArrayList<Pair<String, String>> kunex;
    public final ArrayList<Pair<String, String>> onex;

    public KanjiDetails(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        this.kanji = obj.getString(JSON_JI).charAt(0);

        ArrayList<String> kunyomi = new ArrayList<>(0);
        if (obj.has(JSON_KUNYOMI)) {
            JSONArray jsonKunyomi = obj.getJSONArray(JSON_KUNYOMI);
            for (int i = 0; i <= jsonKunyomi.length() - 1; i++) {
                kunyomi.add(jsonKunyomi.getString(i));
            }
        }
        this.kunyomi = kunyomi;


        ArrayList<String> onyomi = new ArrayList<>(0);
        if (obj.has(JSON_ONYOMI)) {
            JSONArray jsononyomi = obj.getJSONArray(JSON_ONYOMI);
            for (int i = 0; i <= jsononyomi.length() - 1; i++) {
                onyomi.add(jsononyomi.getString(i));
            }
        }
        this.onyomi = onyomi;

        ArrayList<String> kr = new ArrayList<>(0);
        if (obj.has(JSON_KOREAN_READINGS)) {
            JSONArray jsonkr = obj.getJSONArray(JSON_KOREAN_READINGS);
            for (int i = 0; i <= jsonkr.length() - 1; i++) {
                kr.add(jsonkr.getString(i));
            }
        }
        this.kr = kr;

        this.strokes = obj.getInt(JSON_STROKES);

        this.radical = obj.getString(JSON_RADICAL).charAt(0);

        ArrayList<KanjiMeaning> meanings = new ArrayList<>(0);
        if (obj.has(JSON_MEANINGS)) {
            JSONArray jsonmeanings = obj.getJSONArray(JSON_MEANINGS);
            for (int i = 0; i <= jsonmeanings.length() - 1; i++) {
                meanings.add(new KanjiMeaning(jsonmeanings.getJSONObject(i)));
            }
        }

        this.meanings = meanings;

        ArrayList<Pair<String, String>> kunex = new ArrayList<>(0);
        if (obj.has(JSON_KUNEX)) {
            JSONArray jsonkunex = obj.getJSONArray(JSON_KUNEX);
            for (int i = 0; i <= jsonkunex.length() - 1; i++) {
                String word = jsonkunex.getJSONObject(i).getString(JSON_WORDEX_WORD);
                String lnk = jsonkunex.getJSONObject(i).getString(JSON_WORDEX_LNK);
                kunex.add(new Pair<>(word, lnk));
            }
        }

        this.kunex = kunex;


        ArrayList<Pair<String, String>> onex = new ArrayList<>(0);
        if (obj.has(JSON_ONEX)) {
            JSONArray jsononex = obj.getJSONArray(JSON_ONEX);
            for (int i = 0; i <= jsononex.length() - 1; i++) {
                String word = jsononex.getJSONObject(i).getString(JSON_WORDEX_WORD);
                String lnk = jsononex.getJSONObject(i).getString(JSON_WORDEX_LNK);
                onex.add(new Pair<>(word, lnk));
            }
        }

        this.onex = onex;
    }


}
