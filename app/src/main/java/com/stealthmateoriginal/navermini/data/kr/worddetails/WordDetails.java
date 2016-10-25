package com.stealthmateoriginal.navermini.data.kr.worddetails;

import com.stealthmateoriginal.navermini.data.kr.KrWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class WordDetails implements Serializable {

    private static final String JSON_WORD = "word";
    private static final String JSON_HANJA = "hanja";
    private static final String JSON_PRONUN = "pronun";
    private static final String JSON_WORDCLASS = "wordclass";
    private static final String JSON_DEFINITIONS = "defs";


    public final Word word;
    public final ArrayList<Definition> defs;

    public WordDetails(Word word, ArrayList<Definition> defs) {
        this.word = word;
        this.defs = defs;
    }

    public WordDetails(String json) throws JSONException {

        JSONObject obj = new JSONObject(json);

        String name;
        String hanja = null;
        String pronun = null;
        String wordclass = null;

        name = obj.getString(JSON_WORD);
        if(obj.has(JSON_HANJA)) hanja = obj.getString(JSON_HANJA);
        if(obj.has(JSON_PRONUN)) pronun = obj.getString(JSON_PRONUN);
        if(obj.has(JSON_WORDCLASS)) wordclass = obj.getString(JSON_WORDCLASS);

        this.word = new Word(name, pronun, hanja, new String[]{wordclass});

        JSONArray defarr = obj.getJSONArray(JSON_DEFINITIONS);

        ArrayList<Definition> defs = new ArrayList<>(defarr.length());

        for (int i = 0; i <= defarr.length() - 1; i++) {
            JSONObject defobj = defarr.getJSONObject(i);
            String def = defobj.getString("def");

            JSONArray exarr = defobj.getJSONArray("ex");
            ArrayList<String> ex = new ArrayList<>(exarr.length());
            for (int j = 0; j <= exarr.length() - 1; j++) {
                ex.add(exarr.getString(j));
            }

            defs.add(new Definition(def, ex));
        }

        this.defs = defs;
    }
}
