package com.stealthmateoriginal.navermini.data.jp.worddetails;

import com.stealthmateoriginal.navermini.data.TranslatedExample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class Gloss {

    private static final String JSON_GLOSS = "g";
    private static final String JSON_EXAMPLES = "ex";

    private final String gloss;
    private final ArrayList<TranslatedExample> examples;

    Gloss(JSONObject obj) throws JSONException {
        this.gloss = obj.getString(JSON_GLOSS);
        JSONArray jsonexamples = obj.getJSONArray(JSON_EXAMPLES);
        this.examples = new ArrayList<>(jsonexamples.length());
        for (int i = 0; i <= jsonexamples.length() - 1; i++) {
            this.examples.add(new TranslatedExample(jsonexamples.getJSONObject(i)));
        }
    }

    Gloss(String gloss, ArrayList<TranslatedExample> examples) {
        this.gloss = gloss;
        this.examples = examples;
    }

    public String getGloss() {
        return gloss;
    }

    public ArrayList<TranslatedExample> getExamples() {
        return examples;
    }

}