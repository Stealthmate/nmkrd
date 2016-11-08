package com.stealthmatedev.navermini.state;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.stealthmatedev.navermini.R;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public abstract class Autocompleter {

    public interface OnSuggestions {
        void received(ArrayList<String> suggestions);

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
                                ArrayList<String> suglist = parseResponse(response);
                                Collections.sort(suglist, new Comparator<String>() {
                                    @Override
                                    public int compare(String o1, String o2) {
                                        int index1 = o1.indexOf(query);
                                        int index2 = o2.indexOf(query);

                                        if (index1 == -1 && index2 > -1) return +1;
                                        if (index2 == -1 && index1 > -1) return -1;
                                        if (index1 == index2 && index1 == -1) return o2.length() - o1.length();

                                        if (index1 < index2) return -1;
                                        if (index2 < index1 ) return +1;
                                        return o2.length() - o1.length();
                                    }
                                });
                                callback.received(suglist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                callback.received(new ArrayList<String>());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    String msg = "Unknown error";

                    if (error.networkResponse == null) {
                        msg = state.getActivity().getResources().getString(R.string.err_no_internet);
                    } else if (error.getClass().equals(TimeoutError.class)) {
                        msg = "Connection timed out. Is your connection unstable?";
                    } else {
                        int status = error.networkResponse.statusCode;
                        msg = "Error code: " + status;
                    }
                    Toast toast = Toast.makeText(state.getActivity(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                    callback.error(error);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callback.received(new ArrayList<String>());
        }
        req.setTag(TAG);
        req.setShouldCache(true);
        req.setRetryPolicy(new DefaultRetryPolicy(2000, 2, 2));
        queue.add(req);
    }

    protected abstract String getURL(String query) throws UnsupportedEncodingException;

    protected abstract ArrayList<String> parseResponse(String response) throws JSONException;
}
