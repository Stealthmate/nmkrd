package com.stealthmateoriginal.navermini.data.jp;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.jp.JpWordDetailsAdapter;
import com.stealthmateoriginal.navermini.state.DetailsDictionary;
import com.stealthmateoriginal.navermini.state.ResultListItem;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class JpWordEntry implements ResultListItem {

    private static final String NAME = "word";
    private static final String KANJI = "kanji";
    private static final String CLASS = "class";
    private static final String MORE = "more";
    private static final String MEANING = "gloss";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    private String name;
    private String meaning;

    private String kanji;
    private String[] wordclasses;

    private String moreInfo;


    public static JpWordEntry fromJSON(JSONObject obj) throws JSONException {

        String name = obj.getString(NAME);
        String kanji = "";
        String[] classes = new String[0];
        String gloss = obj.getString(MEANING);
        String moreInfo = NO_MORE_INFO;


        if (obj.has(KANJI)) kanji = obj.getString(KANJI);
        if (obj.has(CLASS)) {
            JSONArray json_classes = obj.getJSONArray(CLASS);
            classes = new String[json_classes.length()];
            for (int i = 0; i <= json_classes.length() - 1; i++) {
                classes[i] = json_classes.getString(i);
            }
        }

        if (obj.has(MORE)) moreInfo = obj.getString(MORE);

        return new JpWordEntry(name, kanji, classes, gloss, moreInfo);
    }

    private JpWordEntry(String name, String kanji, String[] wordclasses, String gloss, String moreInfo) {
        this.name = name;
        this.kanji = kanji;
        this.wordclasses = wordclasses;
        this.meaning = gloss;
        this.moreInfo = moreInfo;
    }


    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getKanji() {
        return kanji;
    }

    public String[] getWordClasses() {
        return wordclasses;
    }

    public String getMoreInfoLink() {
        return moreInfo;
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof JpWordEntry)) return false;

        JpWordEntry word = (JpWordEntry) obj;

        if (!name.equals(word.name)) return false;
        if (!meaning.equals(word.meaning)) return false;
        if (!kanji.equals(word.kanji)) return false;

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
        return name + " " + kanji + " " + meaning;
    }

    @Override
    public boolean hasDetails() {
        return !moreInfo.equals(NO_MORE_INFO);
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.JAPANESE_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(moreInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Shit");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public DetailsAdapter createAdapterFromDetails(StateManager state, String details) {
        return new JpWordDetailsAdapter(state, this, details);
    }
}
