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
        new AsyncTask<String, Void, ArrayList<ExampleSentence>>() {
            @Override
            protected ArrayList<ExampleSentence> doInBackground(String... params) {
                Cursor c= store.findByWord(word);
                ArrayList<ExampleSentence> res = new ArrayList<>(c.getCount());
                while(c.moveToNext()) res.add(new ExampleSentence(SentenceEntry.Language.valueOf(c.getString(1)), SentenceEntry.Language.valueOf(c.getString(2)),  c.getString(3), c.getString(4), c.getString(0)));
                return res;
            }

            @Override
            protected void onPostExecute(ArrayList<ExampleSentence> result) {
                callback.callback(result);
            }
        }.execute(word);
    }
}
