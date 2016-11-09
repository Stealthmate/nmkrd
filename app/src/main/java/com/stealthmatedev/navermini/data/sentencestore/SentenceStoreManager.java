package com.stealthmatedev.navermini.data.sentencestore;

import android.database.Cursor;
import android.os.AsyncTask;

import com.stealthmatedev.navermini.data.DBHelper;
import com.stealthmatedev.navermini.data.ExampleSentence;
import com.stealthmatedev.navermini.data.SentenceEntry;

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
        new AsyncTask<String, Void, ArrayList<SentenceEntry>>() {
            @Override
            protected ArrayList<SentenceEntry> doInBackground(String... params) {
                Cursor c= store.findByWord(word);
                ArrayList<SentenceEntry> res = new ArrayList<>(c.getCount());
                while(c.moveToNext()) res.add(new SentenceEntry(SentenceEntry.Language.valueOf(c.getString(1)), SentenceEntry.Language.valueOf(c.getString(2)),  c.getString(3), c.getString(4), c.getString(0)));
                return res;
            }

            @Override
            protected void onPostExecute(ArrayList<SentenceEntry> result) {
                callback.callback(result);
            }
        }.execute(word);
    }

    public void removeAll(final Callback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                store.removeAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                callback.callback(result);
            }
        }.execute();
    }

    public void queryAll(final Callback callback) {
        new AsyncTask<Void, Void, ArrayList<SentenceEntry>>() {
            @Override
            protected ArrayList<SentenceEntry> doInBackground(Void... params) {
                return store.getAll();
            }

            @Override
            protected void onPostExecute(ArrayList<SentenceEntry> result) {
                callback.callback(result);
            }
        }.execute();
    }
}
