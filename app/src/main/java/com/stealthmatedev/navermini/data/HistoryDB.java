package com.stealthmatedev.navermini.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void put(String str) {
        this.getWritableDatabase().execSQL("INSERT INTO " + DICTIONARY_TABLE_NAME  + " VALUES (\"" + str + "\",\"" + str + "\")");
    }

    public void remove(String str) {
        this.getWritableDatabase().execSQL("DELETE FROM " + DICTIONARY_TABLE_NAME + " WHERE " + KEY_WORD + "=\"" + str + "\"");
    }

    public void get(String str) {
        Cursor c = getReadableDatabase().query(
                DICTIONARY_TABLE_NAME,
                null,
                null,
                null,
                null, null, null);
        for(int i=0;i<=c.getCount()-1;i++) {
            c.moveToPosition(i);
        }
    }
}
