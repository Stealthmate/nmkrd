package com.stealthmatedev.navermini.serverapi.en;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class EnWordResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {
        ArrayList<Entry> items = new ArrayList<>();
        EnWord[] words = new Gson().fromJson(response, EnWord[].class);

        for (int i = 0; i < words.length; i++) {
            items.add(words[i]);
        }

        return items;
    }
}
