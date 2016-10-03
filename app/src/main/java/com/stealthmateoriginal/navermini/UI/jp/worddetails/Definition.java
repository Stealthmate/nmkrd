package com.stealthmateoriginal.navermini.UI.jp.worddetails;

import com.stealthmateoriginal.navermini.UI.jp.JpWordDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stealthmate on 16/10/03 0003.
 */
public class Definition {

    public static class Example {
        String original;
        String translated;

        Example(JSONObject obj) throws JSONException {
            this.original = obj.getString("ex");
            this.translated = obj.getString("tr");
        }
    }

    public class Meaning {
        public class Gloss {
            String gloss;Example[] examples;

            Gloss(JSONObject obj) throws JSONException {
                this.gloss = obj.getString("g");
                JSONArray jsonexamples = obj.getJSONArray("ex");
                this.examples = new Example[jsonexamples.length()];
                for (int i = 0; i <= jsonexamples.length() - 1; i++) {
                    this.examples[i] = new Example(jsonexamples.getJSONObject(i));
                }
            }

        }

        String meaning;
        Meaning.Gloss[] glosses;
        Example[] examples;

        Meaning(JSONObject obj) throws JSONException {
            this.meaning = obj.getString("m");

            this.glosses = null;
            this.examples = null;

            if(obj.has("gloss")) {
                JSONArray jsonglosses = obj.getJSONArray("gloss");
                this.glosses = new Meaning.Gloss[jsonglosses.length()];
                for (int i = 0; i <= jsonglosses.length() - 1; i++) {
                    this.glosses[i] = new Meaning.Gloss(jsonglosses.getJSONObject(i));
                }
            }
            else if (obj.has("ex")) {
                JSONArray jsonexamples = obj.getJSONArray("ex");
                this.examples = new Example[jsonexamples.length()];
                for (int i = 0; i <= jsonexamples.length() - 1; i++) {
                    this.examples[i] = new Example(jsonexamples.getJSONObject(i));
                }
            } else {
                System.out.println("WHAT THE FUCK IS THIS DETAILS IN DEFINITION WORD JP");
            }

        }

    }

    private static final String CLASSNAME = "class";
    private static final String MEANINGS = "mean";

    String classname;
    Meaning[] meanings;

    public Definition(JSONObject obj) throws JSONException {
        this.classname = obj.getString(CLASSNAME);
        JSONArray jsonmeanings = obj.getJSONArray(MEANINGS);
        this.meanings = new Meaning[jsonmeanings.length()];
        for (int i = 0; i <= jsonmeanings.length() - 1; i++) {
            this.meanings[i] = new Meaning(jsonmeanings.getJSONObject(i));
        }
    }
}
