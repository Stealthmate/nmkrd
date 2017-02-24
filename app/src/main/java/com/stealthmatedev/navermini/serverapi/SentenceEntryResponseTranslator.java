package com.stealthmatedev.navermini.serverapi;

import android.util.Log;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.SentenceEntry;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;
import java.util.Collections;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 17/02/24 0024.
 */

public class SentenceEntryResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {

        ArrayList<Entry> entriesList = new ArrayList<>();
        SentenceEntry[] entries = new Gson().fromJson(response, SentenceEntry[].class);

        Collections.addAll(entriesList, entries);
        Log.i(APPTAG, response);
        return entriesList;
    }
}
