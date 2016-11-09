package com.stealthmatedev.navermini.data.kr;

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
 * Created by Stealthmate on 16/11/09 0009.
 */

public class KrAutocompleter extends Autocompleter {

    private static final String QUERY_MAKER = "%QUERY%";
    private static final String URL = "http://ac.dic.naver.net/krdic/ac?frm=stdkrdic&oe=utf8&m=0&r=1&st=111&r_lt=111&q=" + QUERY_MAKER;

    @Override
    protected String getURL(String query) throws UnsupportedEncodingException {
        return URL.replace(QUERY_MAKER, URLEncoder.encode(query, "utf8"));
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
                suggestions.add(new AutocompleteSuggestion(itemOfItem.getString(0).trim(), null));
            }
        }

        Log.d(APPTAG, Arrays.toString(suggestions.toArray()));
        return new ArrayList<>(suggestions);
    }
}
