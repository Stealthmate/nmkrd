package com.stealthmatedev.navermini.state;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.AutocompleteSuggestion;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public abstract class Autocompleter {

    public interface OnSuggestions {
        void received(ArrayList<AutocompleteSuggestion> suggestions);

        void error(VolleyError error);
    }

    private static final String TAG = "nm_autocomplete";

    public void getSuggestions(final StateManager state, final String query, final OnSuggestions callback) {

        RequestQueue queue = state.getSearchEngine().getRequestQueue();

        queue.cancelAll(TAG);
        StringRequest req = null;
        try {
            req = new StringRequest(Request.Method.GET, getURL(query),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<AutocompleteSuggestion> suglist = parseResponse(response);
                                Collections.sort(suglist, new Comparator<AutocompleteSuggestion>() {
                                    @Override
                                    public int compare(AutocompleteSuggestion o1, AutocompleteSuggestion o2) {
                                        int index1 = o1.word.indexOf(query);
                                        int index2 = o2.word.indexOf(query);

                                        if (index1 == -1 && index2 > -1) return +1;
                                        if (index2 == -1 && index1 > -1) return -1;
                                        if (index1 == index2 && index1 == -1)
                                            return o1.word.length() - o2.word.length();

                                        if (index1 < index2) return -1;
                                        if (index2 < index1) return +1;
                                        return o1.word.length() - o2.word.length();
                                    }
                                });
                                callback.received(suglist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.received(new ArrayList<AutocompleteSuggestion>());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    callback.error(error);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.received(new ArrayList<AutocompleteSuggestion>());
        }
        req.setTag(TAG);
        req.setShouldCache(true);
        req.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 2));
        queue.add(req);
    }

    protected abstract String getURL(String query) throws UnsupportedEncodingException;

    protected abstract ArrayList<AutocompleteSuggestion> parseResponse(String response) throws JSONException;
}
