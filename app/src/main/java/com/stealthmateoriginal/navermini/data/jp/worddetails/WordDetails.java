package com.stealthmateoriginal.navermini.data.jp.worddetails;

import com.stealthmateoriginal.navermini.data.jp.JpWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class WordDetails {

    private static final String JSON_CLASSNAME = "class";
    private static final String JSON_MEANINGS = "mean";

    private final HashMap<String, ArrayList<Meaning>> classToNewMeaningMap;
    public final JpWordEntry word;

    public WordDetails(JpWordEntry word, String json) throws JSONException {

        this.word = word;
        this.classToNewMeaningMap = new HashMap<>();

        JSONArray classArray = new JSONArray(json);
        for (int i = 0; i <= classArray.length() - 1; i++) {
            JSONObject obj = classArray.getJSONObject(i);

            JSONArray json_NewMeanings = obj.getJSONArray(JSON_MEANINGS);
            ArrayList<Meaning> NewMeanings = new ArrayList<>(json_NewMeanings.length());

            for (int j = 0; j <= json_NewMeanings.length() - 1; j++) {
                NewMeanings.add(new Meaning(json_NewMeanings.getJSONObject(j)));
            }
            this.classToNewMeaningMap.put(obj.getString(JSON_CLASSNAME), NewMeanings);
        }
    }

    public WordDetails(JpWordEntry word) {
        this.word = word;
        this.classToNewMeaningMap = new HashMap<>();
        ArrayList<Meaning> meanings = new ArrayList<>(1);
        meanings.add(new Meaning(word));
        this.classToNewMeaningMap.put(word.getWordClasses()[0], meanings);

        System.out.println(Arrays.toString(meanings.toArray()));
        System.out.println(Arrays.toString(classToNewMeaningMap.keySet().toArray()));
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
