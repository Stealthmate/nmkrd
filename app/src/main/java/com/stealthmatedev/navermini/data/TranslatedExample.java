package com.stealthmatedev.navermini.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */
public class TranslatedExample implements Serializable {

    private static final String EXAMPLE = "ex";
    private static final String TRANSLATED = "tr";

    public final String ex;
    public final String tr;

    public TranslatedExample(JSONObject obj) throws JSONException {
        this.ex = obj.getString(EXAMPLE);
        this.tr = obj.getString(TRANSLATED);
    }

    public TranslatedExample(String ex, String tr) {
        this.ex = ex;
        this.tr = tr;
    }
}