package com.stealthmatedev.navermini.data.sentencestore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.stealthmatedev.navermini.data.DBHelper;
import com.stealthmatedev.navermini.data.SentenceEntry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class SentenceStore implements DBHelper.TableManager {

    private static final String TABLE_NAME = "SentenceStore";
    private static final String INDEX_NAME = "SentenceStoreIndex";

    private static final String COLUMN_KEYWORD = "Keyword";
    private static final String COLUMN_LANG_FROM = "LangFrom";
    private static final String COLUMN_LANG_TO = "LangTo";
    private static final String COLUMN_SENTENCE = "Sentence";
    private static final String COLUMN_TRANSLATION = "Translation";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_KEYWORD + " char(255), " +
                    COLUMN_LANG_FROM + " char(3), " +
                    COLUMN_LANG_TO + " char(3), " +
                    COLUMN_SENTENCE + " text, " +
                    COLUMN_TRANSLATION + " text, " +
                    "PRIMARY KEY (" + COLUMN_KEYWORD + "));";

    private static final String SQL_CREATE_INDEX =
            "CREATE UNIQUE INDEX " + INDEX_NAME + " ON " + " " + TABLE_NAME + "(" + COLUMN_KEYWORD + ");";

    private final DBHelper dbHelper;

    public SentenceStore(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    @Override
    public void onCreate() {
        dbHelper.getWritableDatabase().execSQL(SQL_CREATE_TABLE);
        dbHelper.getWritableDatabase().execSQL(SQL_CREATE_INDEX);
    }

    @Override
    public void onUpdate() {

    }

    public void put(SentenceEntry entry) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LANG_FROM, entry.getLanguages().first.name());
        values.put(COLUMN_LANG_TO, entry.getLanguages().second.name());
        values.put(COLUMN_SENTENCE, entry.getUntranslated());
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor findByWord(String word) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, COLUMN_KEYWORD + "=? OR " + COLUMN_SENTENCE + " LIKE %?%;", new String[]{word, word}, null, null, null);
        return c;
    }
}
