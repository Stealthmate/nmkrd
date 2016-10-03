package com.stealthmateoriginal.navermini.state;

import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class SearchEngine {

    public interface OnResponse {
        void responseReady(String response);
    }

    private static final String TAG = "tag";

    private RequestQueue queue;
    private StateManager state;

    public SearchEngine(StateManager state) {
        this.state = state;
        this.queue = Volley.newRequestQueue(state.getActivity());
    }

    public void request(String url, final OnResponse callback) {
        cancellAllQueries();
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.responseReady(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                int status = error.networkResponse.statusCode;
                String msg = "Unknown error";
                switch(status) {
                    case 404: {
                        msg = "Internal error. Email me what you were doing.";
                    } break;
                }
                Toast toast = Toast.makeText(state.getActivity(), msg, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        req.setTag(TAG);
        req.setShouldCache(true);
        req.setRetryPolicy(new DefaultRetryPolicy(3000, 2, 5));
        queue.add(req);
    }

    public void cancellAllQueries() {
        queue.cancelAll(TAG);
    }
}