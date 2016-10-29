package com.stealthmatedev.navermini.data.kr;

import com.stealthmatedev.navermini.App;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.DetailsDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class KrWordEntry implements DetailedItem {

    private static final String NAME = "word";
    private static final String HANJA = "hanja";
    private static final String PRONUN = "pronun";
    private static final String CLASS = "class";
    private static final String MORE = "more";
    private static final String MEANING = "gloss";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    public final String name;
    public final String meaning;

    public final String hanja;
    public final String pronunciation;
    public final String[] wordclasses;

    public final String moreInfo;


    public static KrWordEntry fromJSON(JSONObject obj) throws JSONException {

        String name = obj.getString(NAME);
        String hanja = "";
        String pronunciation = "";
        String[] classes = new String[0];
        String gloss = obj.getString(MEANING);
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

        return new KrWordEntry(name, hanja, pronunciation, classes, gloss, moreInfo);
    }

    private KrWordEntry(String name, String hanja, String pronunciation, String[] wordclasses, String gloss, String moreInfo) {
        this.name = name;
        this.hanja = hanja;
        this.pronunciation = pronunciation;
        this.wordclasses = wordclasses;
        this.meaning = gloss;
        this.moreInfo = moreInfo;
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof KrWordEntry)) return false;

        KrWordEntry word = (KrWordEntry) obj;

        if (!name.equals(word.name)) return false;
        if (!meaning.equals(word.meaning)) return false;
        if (!hanja.equals(word.hanja)) return false;
        if (!pronunciation.equals(word.pronunciation)) return false;

        if (!moreInfo.equals(word.moreInfo)) return false;

        if (wordclasses.length != word.wordclasses.length) return false;

        for (int i = 0; i <= wordclasses.length - 1; i++) {
            if(!wordclasses[i].equals(word.wordclasses[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + hanja + " " + pronunciation + " " + meaning;
    }

    @Override
    public boolean hasDetails() {
        return !moreInfo.equals(NO_MORE_INFO);
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.KOREAN_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(moreInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
