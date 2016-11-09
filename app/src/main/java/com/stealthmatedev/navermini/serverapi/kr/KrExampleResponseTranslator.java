package com.stealthmatedev.navermini.serverapi.kr;

import android.util.Log;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrExample;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class KrExampleResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {

        ArrayList<Entry> entriesList = new ArrayList<>();
        KrExample[] entries = new Gson().fromJson(response, KrExample[].class);

        Collections.addAll(entriesList, entries);

        return entriesList;
    }
}
