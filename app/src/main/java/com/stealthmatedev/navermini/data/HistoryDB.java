package com.stealthmatedev.navermini.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.data.history.HistoryEntry;

/**
 * Created by Stealthmate on 16/10/28 0028.
 */

public class HistoryDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DB_NAME = "TESTDB";


    private static final String DICTIONARY_TABLE_NAME = "dictionary";

    private static final String KEY_WORD = "WORD";
    private static final String KEY_DEFINITION = "DEF";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    KEY_WORD + " TEXT, " +
                    KEY_DEFINITION + " TEXT);";

    public HistoryDB(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static final String INSERTION_SQL = "INSERT INTO " + DICTIONARY_TABLE_NAME  + " VALUES (\"%DATA\")";

    public void put(Entry entry) {
        this.getWritableDatabase().execSQL("INSERT INTO " + DICTIONARY_TABLE_NAME + " VALUES (" + DatabaseUtils.sqlEscapeString(new HistoryEntry(entry).getJson()) + ")");
    }

    public void remove(String str) {
        this.getWritableDatabase().execSQL("DELETE FROM " + DICTIONARY_TABLE_NAME + " WHERE " + KEY_WORD + "=\"" + str + "\"");
    }

    private Entry parseJson(String json) {
        HistoryEntry entry = new Gson().fromJson(json, HistoryEntry.class);
        return entry.data;
    }

    public Entry get(int id) {
        Cursor c = getReadableDatabase().query(
                DICTIONARY_TABLE_NAME,
                null,
                null,
                null,
                null, null, null);
        c.moveToPosition(id);

        String json = c.getString(1);
        c.close();
        return parseJson(json);
    }
}
