package com.stealthmateoriginal.navermini.data.jp.worddetails;

import com.stealthmateoriginal.navermini.data.TranslatedExample;
import com.stealthmateoriginal.navermini.data.jp.JpWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class Meaning implements Serializable {

    private static final String JSON_MEANING = "m";
    private static final String JSON_GLOSSES = "gloss";
    private static final String JSON_EXAMPLES = "ex";

    private final String meaning;
    private final ArrayList<Gloss> glosses;

    Meaning(JSONObject obj) throws JSONException {
        String meaning = obj.getString(JSON_MEANING);
        this.glosses = new ArrayList<>(1);
        boolean hasGlosses = obj.has(JSON_GLOSSES);

        ArrayList<TranslatedExample> examples = new ArrayList<>();

        if (hasGlosses) {
            JSONArray jsonglosses = obj.getJSONArray(JSON_GLOSSES);
            for (int i = 0; i <= jsonglosses.length() - 1; i++) {
                glosses.add(new Gloss(jsonglosses.getJSONObject(i)));
            }
        } else if (obj.has(JSON_EXAMPLES)) {
            JSONArray jsonexamples = obj.getJSONArray(JSON_EXAMPLES);
            examples = new ArrayList<>(jsonexamples.length());
            for (int i = 0; i <= jsonexamples.length() - 1; i++) {
                examples.add(new TranslatedExample(jsonexamples.getJSONObject(i)));
            }
        }

        this.meaning = meaning;
        glosses.add(0, new Gloss(null, examples));
    }

    Meaning(JpWordEntry word) {
        this.meaning = word.getMeaning();
        this.glosses = new ArrayList<>(1);
        this.glosses.add(new Gloss(null, new ArrayList<TranslatedExample>(0)));
    }

    public ArrayList<Gloss> getGlosses() {
        return glosses;
    }

    public String getMeaning() {
        return meaning;
    }
}
