package com.stealthmatedev.navermini.data.en;

import android.util.Log;

import com.stealthmatedev.navermini.data.AutocompleteSuggestion;
import com.stealthmatedev.navermini.state.Autocompleter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public class EnAutocompleter extends Autocompleter {

    private static final String QUERY_MARKER = "%QUERY%";
    private static final String URL = "http://ac.endic.naver.com/ac?q_enc=utf-8&st=11001&r_format=json&r_enc=utf-8&r_lt=10001&r_unicode=0&r_escape=1&q=" + QUERY_MARKER;

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

        Log.d(APPTAG, Arrays.toString(suggestions.toArray()));
        return new ArrayList<>(suggestions);
    }
}
