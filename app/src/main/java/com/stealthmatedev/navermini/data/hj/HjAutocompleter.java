package com.stealthmatedev.navermini.data.hj;

import com.stealthmatedev.navermini.data.AutocompleteSuggestion;
import com.stealthmatedev.navermini.state.Autocompleter;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/28 0028.
 */

public class HjAutocompleter extends Autocompleter {
    @Override
    protected String getURL(String query) throws UnsupportedEncodingException {
        return null;
    }

    @Override
    protected ArrayList<AutocompleteSuggestion> parseResponse(String response) throws JSONException {
        return null;
    }
}
