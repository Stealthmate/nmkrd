package com.stealthmatedev.navermini.data.sentencestore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stealthmatedev.navermini.data.CallbackAsyncTask;
import com.stealthmatedev.navermini.data.DBHelper;
import com.stealthmatedev.navermini.data.SentenceEntry;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class SentenceStoreTableManager extends DBHelper.TableManager {

    private static final String TABLE_NAME = "SentenceStoreTableManager";
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
                    "PRIMARY KEY (" + COLUMN_SENTENCE + "));";

    private static final String SQL_CREATE_INDEX =
            "CREATE UNIQUE INDEX " + INDEX_NAME + " ON " + " " + TABLE_NAME + "(" + COLUMN_SENTENCE + ");";

    public SentenceStoreTableManager(DBHelper dbHelper) {
        super(dbHelper);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_INDEX);
    }

    @Override
    public void onUpdate() {

    }

    public void put(SentenceEntry entry, final CallbackAsyncTask.Callback callback) {
        new CallbackAsyncTask<SentenceEntry, Void, Void>(callback) {
            @Override
            protected Void doInBackground(SentenceEntry... params) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEYWORD, params[0].keyword);
                values.put(COLUMN_LANG_FROM, params[0].from.name());
                values.put(COLUMN_LANG_TO, params[0].to.name());
                values.put(COLUMN_SENTENCE, params[0].ex);
                values.put(COLUMN_TRANSLATION, params[0].tr);
                Log.d(APPTAG, values.toString());
                db.replace(TABLE_NAME, null, values);
                return null;
            }
        }.execute(entry);
    }

    public void getAll(CallbackAsyncTask.Callback callback) {
        new CallbackAsyncTask<Void, Void, ArrayList<SentenceEntry>>(callback) {
            @Override
            protected ArrayList<SentenceEntry> doInBackground(Void... params) {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
                ArrayList<SentenceEntry> entries = new ArrayList<>(c.getCount());
                while (c.moveToNext()) {
                    entries.add(new SentenceEntry(SentenceEntry.Language.valueOf(c.getString(1)), SentenceEntry.Language.valueOf(c.getString(2)), c.getString(0), c.getString(3), c.getString(4)));
                }
                c.close();
                return entries;
            }
        }.execute();
    }

    public void remove(SentenceEntry entry, CallbackAsyncTask.Callback callback) {
        new CallbackAsyncTask<SentenceEntry, Void, Void>(callback) {
            @Override
            protected Void doInBackground(SentenceEntry... params) {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(TABLE_NAME, COLUMN_SENTENCE + "=?", new String[]{params[0].ex});
                return null;
            }
        }.execute(entry);
    }

    public void removeAll(CallbackAsyncTask.Callback callback) {
        new CallbackAsyncTask<Void, Void, Void>(callback) {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(TABLE_NAME, null, null);
                return null;
            }
        }.execute();
    }
}
