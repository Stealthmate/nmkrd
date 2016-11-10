package com.stealthmatedev.navermini.data.jp;

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
 * Created by Stealthmate on 16/11/10 0010.
 */

public class JpAutocompleter extends Autocompleter {

    private static final String QUERY_MARKER = "%QUERY%";
    private final String URL = "http://jpdic.naver.com/ac?st=111&r_lt=111&n_kojpdic=1&q=%QUERY%";


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
