package com.stealthmatedev.navermini.data.history;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.AsyncTask;

import com.stealthmatedev.navermini.data.DetailedEntry;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.R.id.result;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class HistoryManager {

    public interface Observer {
        void onChanged();
        void onInvalidated();
    }

    public interface Callback {
        void onFinish(Object result);
    }


    public static final String VERSION = "1.0";

    private HistoryDBHelper dbHelper;
    private ArrayList<Observer> observers;

    public HistoryManager(Context context) {
        this.observers = new ArrayList<>();
        dbHelper = new HistoryDBHelper(context);
    }

    public HistoryDBHelper getDbHelper() {
        return dbHelper;
    }

    public void get(DetailedEntry entry, final Callback callback) {
        new AsyncTask<Object, Void, DetailedEntry>() {
            @Override
            protected DetailedEntry doInBackground(Object... params) {
                DetailedEntry entry = (DetailedEntry) params[0];
                HistoryEntry he = dbHelper.findById(new HistoryEntry(entry).getId());
                return he != null ? he.data : null;
            }

            @Override
            protected void onPostExecute(DetailedEntry result) {
                callback.onFinish(result);
            }
        }.execute(entry, callback);
    }

    public void getAll(final Callback callback) {
        new AsyncTask<Void, Void, ArrayList<DetailedEntry>>() {
            @Override
            protected ArrayList<DetailedEntry> doInBackground(Void... params) {
                ArrayList<DetailedEntry> arr = dbHelper.queryAll();
                return arr;
            }

            @Override
            protected void onPostExecute(ArrayList<DetailedEntry> result) {
                callback.onFinish(result);
            }
        }.execute();
    }

    public void getCursorToAll(final Callback callback) {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                Cursor c = dbHelper.getCursor();
                if (c.getCount() > 0) c.moveToFirst();
                return c;
            }

            @Override
            protected void onPostExecute(Cursor result) {
                callback.onFinish(result);
            }
        }.execute();
    }

    public void save(DetailedEntry entry) {
        new AsyncTask<DetailedEntry, Void, Void>() {
            @Override
            protected Void doInBackground(DetailedEntry... params) {
                dbHelper.put(params[0]);
                notifyChanged();
                return null;
            }
        }.execute(entry);
    }

    public void clearHistory() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                dbHelper.deleteAll();
                notifyChanged();
                return null;
            }
        }.execute();
    }

    public void removeEntry(final DetailedEntry entry) {
        new AsyncTask<DetailedEntry, Void, Void>() {

            @Override
            protected Void doInBackground(DetailedEntry... params) {
                dbHelper.deleteSingle(params[0]);
                notifyChanged();
                return null;
            }
        }.execute(entry);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyChanged() {
        for (Observer obs : observers) obs.onChanged();
    }
}
