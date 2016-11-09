package com.stealthmatedev.navermini.data;

import android.os.AsyncTask;

/**
 * Created by Stealthmate on 16/11/09 0009.
 */

public abstract class CallbackAsyncTask<Arguments, Progress, Result> extends AsyncTask<Arguments, Progress, Result> {

    public interface Callback {
        void callback(Object param);
    }

    private Callback callback;

    public CallbackAsyncTask(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onPostExecute(Result result) {
        if(callback != null) this.callback.callback(result);
    }

}
