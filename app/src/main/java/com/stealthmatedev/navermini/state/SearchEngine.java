package com.stealthmatedev.navermini.state;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stealthmatedev.navermini.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.stealthmatedev.navermini.App.APPTAG;
import static com.stealthmatedev.navermini.App.HOST;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class SearchEngine {

    public interface OnResponse {
        void responseReady(String response);
        void onError(VolleyError err);
    }

    private static final String TAG = "tag";

    private static final String PARAM_QUERY = "q";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PAGESIZE = "pagesize";

    private RequestQueue queue;
    private StateManager state;

    public SearchEngine(StateManager state) {
        this.state = state;
        this.queue = Volley.newRequestQueue(state.getActivity());
    }

    private void request(final String url, final OnResponse callback) {
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

                String msg = "Unknown error";

                if(error.networkResponse == null) {
                    msg = state.getActivity().getResources().getString(R.string.err_no_internet);
                }
                else if(error.getClass().equals(TimeoutError.class)) {
                    msg = "Connection timed out. Is your connection unstable?";
                }
                else {
                    int status = error.networkResponse.statusCode;
                    msg = "Error code: " + status;
                }
                Toast toast = Toast.makeText(state.getActivity(), msg, Toast.LENGTH_SHORT);
                toast.show();
                callback.onError(error);
            }
        });
        req.setTag(TAG);
        req.setShouldCache(true);
        req.setRetryPolicy(new DefaultRetryPolicy(20000, 2, 1));
        queue.add(req);
    }

    public RequestQueue getRequestQueue() {
        return queue;
    }

    private void cancellAllQueries() {
        queue.cancelAll(TAG);
    }

    public void queryResultList(ResultListQuery query, OnResponse callback) {
        String url = HOST;
        url += query.path;
        url += "?" + PARAM_PAGE + "=" + query.page;
        url += "&" + PARAM_PAGESIZE + "=" +  query.pagesize;
        try {
            url += "&" + PARAM_QUERY + "=" +  URLEncoder.encode(query.query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(APPTAG, "Unsupported utf-8.");
        }

        request(url, callback);
    }

    public void queryDetails(final String path, OnResponse callback) {
        if(path.startsWith("http")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            state.getActivity().startActivity(browserIntent);
            return;
        }

        request(HOST + path, callback);
    }
}