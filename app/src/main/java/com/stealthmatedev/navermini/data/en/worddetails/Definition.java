package com.stealthmatedev.navermini.data.en.worddetails;

import com.stealthmatedev.navermini.data.TranslatedExample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class Definition implements Serializable {

    private static final String JSON_DEFINITION = "def";
    private static final String JSON_EXAMPLES = "ex";

    public String eng;
    public final String def;
    public final ArrayList<TranslatedExample> ex;

    public Definition() {
        this.def = null;
        this.eng = null;
        this.ex = null;
    }

    public Definition(JSONObject obj) throws JSONException {

        def = obj.getString(JSON_DEFINITION);

        if(obj.has(JSON_EXAMPLES)) {
            JSONArray exarr = obj.getJSONArray(JSON_EXAMPLES);
            ex = new ArrayList<>(exarr.length());

            for (int i = 0; i <= exarr.length() - 1; i++) {
                ex.add(new TranslatedExample(exarr.getJSONObject(i)));
            }
        }
        else {
            ex = null;
        }
    }

    public Definition(String def, ArrayList<TranslatedExample> ex) {
        this.def = def;
        this.ex = ex;
    }
}
