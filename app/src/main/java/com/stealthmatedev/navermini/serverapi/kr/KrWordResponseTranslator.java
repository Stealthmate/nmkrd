package com.stealthmatedev.navermini.serverapi.kr;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class KrWordResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {

        ArrayList<Entry> items = new ArrayList<>();
        KrWord[] words = new Gson().fromJson(response, KrWord[].class);

        for (int i = 0; i < words.length; i++) {
            items.add(words[i]);
        }

        return items;
    }
}