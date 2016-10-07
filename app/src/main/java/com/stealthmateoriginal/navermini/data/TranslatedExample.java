package com.stealthmateoriginal.navermini.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */
public class TranslatedExample {

    private static final String EXAMPLE = "ex";
    private static final String TRANSLATED = "tr";

    public final String original;
    public final String translated;

    public TranslatedExample(JSONObject obj) throws JSONException {
        this.original = obj.getString(EXAMPLE);
        this.translated = obj.getString(TRANSLATED);
    }
}