package com.stealthmateoriginal.navermini.data.jp.kanjidetails;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/21 0021.
 */

public class KanjiMeaning {

    private static final String JSON_MEANING = "m";
    private static final String JSON_EX = "ex";
    private static final String JSON_EX_KR = "kr";
    private static final String JSON_EX_JP = "jp";

    public final String meaning;
    public final ArrayList<Pair<String, String>> ex;


    KanjiMeaning(JSONObject obj) throws JSONException {

        this.meaning = obj.getString(JSON_MEANING);
        ArrayList<Pair<String, String>> ex = new ArrayList<>(0);
        if (obj.has(JSON_EX)) {
            JSONArray jsonEx = obj.getJSONArray(JSON_EX);
            for (int i = 0; i <= jsonEx.length() - 1; i++) {
                String word = jsonEx.getJSONObject(i).getString(JSON_EX_KR);
                String lnk = jsonEx.getJSONObject(i).getString(JSON_EX_JP);
                ex.add(new Pair<>(word, lnk));
            }
        }

        this.ex = ex;
    }


}
