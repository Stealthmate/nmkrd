package com.stealthmatedev.navermini.serverapi.jp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class JpWordKanjiResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {

        ArrayList<Entry> entriesList = new ArrayList<>();

        Gson gson = new GsonBuilder().registerTypeAdapter(Entry.class, new JpWordKanjiDeserializer()).create();

        Entry[] entries = gson.fromJson(response, Entry[].class);
        Collections.addAll(entriesList, entries);

        return entriesList;
    }
}
