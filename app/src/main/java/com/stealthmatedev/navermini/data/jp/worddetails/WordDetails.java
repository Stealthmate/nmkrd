package com.stealthmatedev.navermini.data.jp.worddetails;

import com.stealthmatedev.navermini.data.jp.JpWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class WordDetails implements Serializable {

    private static final String JSON_WORD = "word";
    private static final String JSON_KANJI = "kanji";
    private static final String JSON_CLASSLIST = "meanings";
    private static final String JSON_CLASSNAME = "class";
    private static final String JSON_MEANINGS = "mean";

    public final String word;
    public final String kanji;
    private final HashMap<String, ArrayList<Meaning>> classToNewMeaningMap;

    public WordDetails(String json) throws JSONException {

        this.classToNewMeaningMap = new HashMap<>();

        JSONObject obj = new JSONObject(json);
        this.word = obj.getString(JSON_WORD);
        this.kanji = obj.getString(JSON_KANJI);

        JSONArray classArray = obj.getJSONArray(JSON_CLASSLIST);
        for (int i = 0; i <= classArray.length() - 1; i++) {
            JSONObject clsobj = classArray.getJSONObject(i);

            JSONArray json_NewMeanings = clsobj.getJSONArray(JSON_MEANINGS);
            ArrayList<Meaning> NewMeanings = new ArrayList<>(json_NewMeanings.length());

            for (int j = 0; j <= json_NewMeanings.length() - 1; j++) {
                NewMeanings.add(new Meaning(json_NewMeanings.getJSONObject(j)));
            }
            this.classToNewMeaningMap.put(clsobj.getString(JSON_CLASSNAME), NewMeanings);
        }
    }

    public WordDetails(JpWordEntry word) {

        this.word = word.getName();
        this.kanji = word.getKanji();

        this.classToNewMeaningMap = new HashMap<>();
        ArrayList<Meaning> meanings = new ArrayList<>(1);
        meanings.add(new Meaning(word));
        this.classToNewMeaningMap.put(word.getWordClasses()[0], meanings);
    }

    public Set<String> getWordclasses() {
        return classToNewMeaningMap.keySet();
    }

    public ArrayList<Meaning> getMeaningsForWordclass(String wordclass) {
        return classToNewMeaningMap.get(wordclass);
    }

    public ArrayList<String> getCompactMeaningsForWordclass(String wordclass) {
        ArrayList<Meaning> meanings = getMeaningsForWordclass(wordclass);
        ArrayList<String> meaningStrs = new ArrayList<>(meanings.size());
        for(Meaning m : meanings) {
            meaningStrs.add(m.getMeaning());
        }

        return meaningStrs;
    }

}
