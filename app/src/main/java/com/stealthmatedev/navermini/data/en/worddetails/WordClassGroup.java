package com.stealthmatedev.navermini.data.en.worddetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class WordClassGroup implements Serializable {

    private static final String JSON_WORDCLASS = "class";
    private static final String JSON_DEFS = "clsgrps";

    public final String wclass;
    public final ArrayList<Definition> defs;

    public WordClassGroup(JSONObject obj) throws JSONException {

        String wordclass = null;
        ArrayList<Definition> defs = null;

        if (obj.has(JSON_WORDCLASS)) wordclass = obj.getString(JSON_WORDCLASS);

        JSONArray defarr = obj.getJSONArray(JSON_DEFS);
        defs = new ArrayList<>(defarr.length());

        for (int i = 0; i <= defarr.length() - 1; i++) {
            defs.add(new Definition(defarr.getJSONObject(i)));
        }

        this.wclass = wordclass;
        this.defs = defs;
    }

    public WordClassGroup(String wordclass, ArrayList<Definition> defs) {
        this.wclass = wordclass;
        this.defs = defs;
    }

}
