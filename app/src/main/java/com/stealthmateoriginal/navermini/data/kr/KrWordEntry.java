package com.stealthmateoriginal.navermini.data.kr;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.kr.KrDetailsAdapter;
import com.stealthmateoriginal.navermini.state.DetailsDictionary;
import com.stealthmateoriginal.navermini.state.ResultListItem;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class KrWordEntry implements ResultListItem {

    private static final String NAME = "word";
    private static final String HANJA = "hanja";
    private static final String PRONUN = "pronun";
    private static final String CLASS = "class";
    private static final String MORE = "more";
    private static final String MEANING = "gloss";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    private String name;
    private String meaning;

    private String hanja;
    private String pronunciation;
    private String[] wordclasses;

    private String moreInfo;


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


    public String getName() {
        return name;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getHanja() {
        return hanja;
    }

    public String[] getWordClasses() {
        return wordclasses;
    }

    public String getMoreInfoLink() {
        return moreInfo;
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

        System.out.println("So far so good");

        if (wordclasses.length != word.wordclasses.length) return false;

        System.out.println("Very good");

        for (int i = 0; i <= wordclasses.length - 1; i++) {
            if(!wordclasses[i].equals(word.wordclasses[i])) {
                System.out.println("Fail " + wordclasses[i] + " " + word.wordclasses[i]);
                return false;
            }
        }
        System.out.println("Equal!");
        return true;
    }

    @Override
    public String toString() {
        return name + " " + hanja + " " + pronunciation + " " + meaning;
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.KOREAN_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(moreInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Shit");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public DetailsAdapter createAdapterFromDetails(StateManager state, String details) {

        if(moreInfo.equals(NO_MORE_INFO)) {
            details = "[{\"def\":\"" + meaning + "\", \"ex\":[]}]";
        }

        return new KrDetailsAdapter(state, this, details);
    }
}
