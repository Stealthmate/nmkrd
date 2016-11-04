package com.stealthmatedev.navermini.data.history;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.DetailedEntry;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DB_NAME = "TESTDB";
    public static final String HISTORY_DATABASE_NAME = "History";

    private static final String HISTORY_TABLE_NAME = "history";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DATA = "DATA";

    private static final String SQL_CREATE =
            "CREATE TABLE " + HISTORY_TABLE_NAME + " (" +
                    COLUMN_ID + " char(64), " +
                    COLUMN_DATA + " text"
                    + "," +
                    "PRIMARY KEY (" + COLUMN_ID + "));";

    private static final String SQL_DELETE_ALL =
            "DELETE FROM " + HISTORY_TABLE_NAME;

    private static final String SQL_QUERY_BY_ID =
            "SELECT \"" + COLUMN_DATA + "\"" +
                    " FROM \"" + HISTORY_TABLE_NAME + "\"" +
                    " WHERE \"" + COLUMN_ID + "\"=?";

    private static final String SQL_QUERY_ALL =
            "SELECT \"" + COLUMN_DATA + "\"" +
                    " FROM \"" + HISTORY_TABLE_NAME + "\" ORDER BY " + COLUMN_ID + " DESC ;";

    private static final String SQL_PUT_ENTRY =
            "INSERT OR REPLACE INTO \"" + HISTORY_TABLE_NAME + "\" (" + COLUMN_ID + "," + COLUMN_DATA + ")  VALUES (?, ?);";

    private final SQLiteStatement queryById;
    private final SQLiteStatement putEntry;

    public HistoryDBHelper(Context context) {
        super(context, HISTORY_DATABASE_NAME, null, DATABASE_VERSION);
        queryById = getReadableDatabase().compileStatement(SQL_QUERY_BY_ID);
        putEntry = getWritableDatabase().compileStatement(SQL_PUT_ENTRY);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        Log.d(APPTAG, "Created DB.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private DetailedEntry parseJson(String json) {
        HistoryEntry entry = new Gson().fromJson(json, HistoryEntry.class);
        return entry.data;
    }

    public HistoryEntry findById(String id) {
        queryById.clearBindings();
        queryById.bindString(1, id);
        String data;
        try {
            data = queryById.simpleQueryForString();
        } catch (SQLiteDoneException ex) {
            Log.d(APPTAG, "Could not find entry with ID " + id);
            return null;
        }
        return new GsonBuilder().registerTypeAdapter(HistoryEntry.class, new HistoryEntry.Deserializer()).create().fromJson(data, HistoryEntry.class);
    }

    public void put(DetailedEntry entry) {
        HistoryEntry he = new HistoryEntry(entry);
        Log.d(APPTAG, "Put entry " + he.getId());
        putEntry.clearBindings();
        putEntry.bindString(1, he.getId());
        putEntry.bindString(2, he.getJson());
        putEntry.execute();
    }

    public ArrayList<DetailedEntry> queryAll() {
        Cursor c = getReadableDatabase().rawQuery(SQL_QUERY_ALL, null);
        ArrayList<DetailedEntry> arr = new ArrayList<>(c.getCount());
        if(c.getCount() == 0) return arr;
        c.moveToFirst();
        do {
            arr.add(HistoryEntry.fromJson(c.getString(0)).data);
        } while (c.moveToNext());

        return arr;
    }

    public Cursor getCursor() {
        return getReadableDatabase().rawQuery(SQL_QUERY_ALL, null);
    }

    public void deleteAll() {
        getWritableDatabase().execSQL(SQL_DELETE_ALL);
    }

}
