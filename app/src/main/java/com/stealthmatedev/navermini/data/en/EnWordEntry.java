package com.stealthmatedev.navermini.data.en;

import android.content.Context;

import com.stealthmatedev.navermini.UI.DetailsAdapter;
import com.stealthmatedev.navermini.data.kr.KrWordEntry;
import com.stealthmatedev.navermini.state.DetailedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stealthmate on 16/10/28 0028.
 */

public class EnWordEntry implements DetailedItem {

    private static final String NAME = "word";
    private static final String HANJA = "hanja";
    private static final String PRONUN = "pronun";
    private static final String CLASS = "class";
    private static final String MORE = "more";
    private static final String MEANING = "meaning";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    public static EnWordEntry fromJSON(JSONObject obj) throws JSONException {

        String name = obj.getString(NAME);
        String hanja = null;
        String pronunciation = null;
        String[] classes = new String[0];
        String meaning = obj.getString(MEANING);
        String moreInfo = NO_MORE_INFO;


        if (obj.has(HANJA)) hanja = obj.getString(HANJA);
        if (obj.has(PRONUN)) pronunciation = obj.getString(PRONUN);
        if (obj.has(CLASS)) {
            JSONArray json_classes = obj.getJSONArray(CLASS);
            classes = new String[json_classes.length()];
            for (int i = 0; i <= json_classes.length() - 1; i++) {
                classes[i] = json_classes.getString(i);
            }
        }

        if (obj.has(MORE)) moreInfo = obj.getString(MORE);

        return new EnWordEntry(name, hanja, pronunciation, classes, meaning, moreInfo);
    }

    public final String name;
    public final String hanja;
    public final String pronunciation;
    public final String[] wordclasses;
    public final String meaning;
    public final String moreInfo;

    private EnWordEntry(String name, String hanja, String pronunciation, String[] wordclasses, String meaning, String moreInfo) {
        this.name = name;
        this.hanja = hanja;
        this.pronunciation = pronunciation;
        this.wordclasses = wordclasses;
        this.meaning = meaning;
        this.moreInfo = moreInfo;
    }

    public String getExtra() {
        if(hanja != null && hanja.length() > 0) return hanja;
        else return pronunciation;
    }

    @Override
    public boolean hasDetails() {
        return false;
    }

    @Override
    public String getLinkToDetails() {
        return null;
    }

    @Override
    public DetailsAdapter createAdapterFromDetails(Context context, String details) {
        return null;
    }
}
