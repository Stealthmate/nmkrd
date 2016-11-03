package com.stealthmatedev.navermini.serverapi.kr;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrExample;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class KrExampleResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {

        ArrayList<Entry> entriesList = new ArrayList<>();
        String[] entries = new Gson().fromJson(response, String[].class);

        for (int i = 0; i <= entries.length - 1; i++) entriesList.add(new KrExample(entries[i]));

        return entriesList;
    }
}
