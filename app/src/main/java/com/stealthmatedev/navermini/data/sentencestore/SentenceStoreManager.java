package com.stealthmatedev.navermini.data.sentencestore;

import android.database.Cursor;
import android.os.AsyncTask;

import com.stealthmatedev.navermini.data.DBHelper;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public class SentenceStoreManager {
    public interface Callback {
        void callback(Object result);
    }

    private SentenceStore store;

    public SentenceStoreManager(DBHelper helper) {
        this.store = helper.sentenceStore;
    }

    public void queryByWord(final String word, final Callback callback) {
        new AsyncTask<String, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(String... params) {
                Cursor c= store.findByWord(word);
                ArrayList<String> res = new ArrayList<>(c.getCount());
                while(c.moveToNext()) res.add(c.getString(3));
                return res;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                callback.callback(result);
            }
        }.execute(word);
    }
}
