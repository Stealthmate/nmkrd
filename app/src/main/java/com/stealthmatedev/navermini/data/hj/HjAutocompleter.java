package com.stealthmatedev.navermini.data.hj;

import com.stealthmatedev.navermini.data.AutocompleteSuggestion;
import com.stealthmatedev.navermini.state.Autocompleter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Stealthmate on 16/11/28 0028.
 */

public class HjAutocompleter extends Autocompleter {

    private static final String QUERY_MARKER = "%QUERY%";
    private final String URL = "http://hanja.naver.com/ac?q=" + QUERY_MARKER + "&st=111";

    @Override
    protected String getURL(String query) throws UnsupportedEncodingException {
        return URL.replace(QUERY_MARKER, URLEncoder.encode(query, "utf8"));
    }

    @Override
    protected ArrayList<AutocompleteSuggestion> parseResponse(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        JSONArray items = obj.getJSONArray("items");
        Set<AutocompleteSuggestion> suggestions = new LinkedHashSet<>();
        for (int i = 0; i < items.length(); i++) {
            JSONArray item = items.getJSONArray(i);
            for (int j = 0; j < item.length() ; j++) {
                JSONArray itemOfItem = item.getJSONArray(j);
                suggestions.add(new AutocompleteSuggestion(itemOfItem.getJSONArray(0).getString(0).trim(), itemOfItem.getJSONArray(1).getString(0).trim()));
            }
        }

        return new ArrayList<>(suggestions);
    }
}
