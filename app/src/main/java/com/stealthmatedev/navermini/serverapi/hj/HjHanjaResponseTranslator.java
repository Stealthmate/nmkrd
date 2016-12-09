package com.stealthmatedev.navermini.serverapi.hj;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.hj.HjHanja;
import com.stealthmatedev.navermini.serverapi.ResponseTranslator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/11/28 0028.
 */

public class HjHanjaResponseTranslator implements ResponseTranslator {
    @Override
    public ArrayList<Entry> translate(String response) {
        return new ArrayList<Entry>(Arrays.asList(new Gson().fromJson(response, HjHanja[].class)));
    }
}
