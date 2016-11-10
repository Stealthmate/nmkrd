package com.stealthmatedev.navermini.data.history;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.CallbackAsyncTask;
import com.stealthmatedev.navermini.data.DBHelper;
import com.stealthmatedev.navermini.data.DetailedEntry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/09 0009.
 */

public class HistoryTableManager extends DBHelper.TableManager {

    private static final String TABLE_NAME = "History";
    private static final String TABLE_INDEX = "HistoryIndex";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DATA = "DATA";
    private static final String COLUMN_SEEN = "SEEN";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " char(64), " +
                    COLUMN_DATA + " text," +
                    COLUMN_SEEN + " integer"
                    + "," +
                    "PRIMARY KEY (" + COLUMN_ID + "));";

    private static final String SQL_CREATE_INDEX =
            "CREATE UNIQUE INDEX " + TABLE_INDEX + " ON " + " " + TABLE_NAME + "(" + COLUMN_ID + ");";


    public HistoryTableManager(DBHelper helper) {
        super(helper);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_INDEX);
    }

    @Override
    public void onUpdate() {

    }

    public void findById(String id, CallbackAsyncTask.Callback callback) {
        new CallbackAsyncTask<String, Void, DetailedEntry>(callback) {
            @Override
            protected DetailedEntry doInBackground(String... params) {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor c = db.query(TABLE_NAME, new String[]{COLUMN_DATA}, COLUMN_ID + "=?", new String[]{params[0]}, null, null, COLUMN_SEEN + " DESC");

                HistoryEntry result = null;

                if (c.getCount() > 0) {
                    c.moveToFirst();
                    result = new GsonBuilder().registerTypeAdapter(HistoryEntry.class, new HistoryEntry.Deserializer()).create().fromJson(c.getString(0), HistoryEntry.class);
                }
                c.close();
                return result != null ? result.data : null;
            }
        }.execute(id);
    }

    public void put(DetailedEntry entry, CallbackAsyncTask.Callback callback) {

        new CallbackAsyncTask<DetailedEntry, Void, Void>(callback) {
            @Override
            protected Void doInBackground(DetailedEntry... params) {
                HistoryEntry he = new HistoryEntry(params[0]);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{he.getId()});
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, he.getId());
                values.put(COLUMN_DATA, he.getJson());
                values.put(COLUMN_SEEN, System.currentTimeMillis() / 1000);
                db.replace(TABLE_NAME, null, values);
                notifyChanged();
                return null;
            }
        }.execute(entry);

    }

    public void queryAll(CallbackAsyncTask.Callback callback) {

        new CallbackAsyncTask<Void, Void, ArrayList<DetailedEntry>>(callback) {
            @Override
            protected ArrayList<DetailedEntry> doInBackground(Void... params) {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor c = db.query(TABLE_NAME, new String[]{COLUMN_DATA}, null, null, null, null, COLUMN_SEEN + " DESC");
                ArrayList<DetailedEntry> arr = new ArrayList<>(c.getCount());
                if (c.getCount() == 0) return arr;
                c.moveToFirst();
                do {
                    arr.add(HistoryEntry.fromJson(c.getString(0)).data);
                } while (c.moveToNext());

                c.close();

                return arr;
            }
        }.execute();
    }

    public void deleteAll(CallbackAsyncTask.Callback callback) {

        new CallbackAsyncTask<Void, Void, Void>(callback) {
            @Override
            protected Void doInBackground(Void... params) {
                helper.getWritableDatabase().delete(TABLE_NAME, null, null);
                notifyChanged();
                return null;
            }
        }.execute();

    }

    public void delete(DetailedEntry entry, CallbackAsyncTask.Callback callback) {

        new CallbackAsyncTask<DetailedEntry, Void, Void>(callback) {
            @Override
            protected Void doInBackground(DetailedEntry... params) {
                ContentValues values = new ContentValues();
                HistoryEntry he = new HistoryEntry(params[0]);
                values.put(COLUMN_ID, he.getId());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{he.getId()});
                notifyChanged();
                return null;
            }
        }.execute(entry);
    }
}
