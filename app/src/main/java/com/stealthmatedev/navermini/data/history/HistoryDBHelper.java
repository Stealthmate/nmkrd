package com.stealthmatedev.navermini.data.history;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
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
    private static final String HISTORY_TABLE_INDEX = "historyindex";

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_DATA = "DATA";
    private static final String COLUMN_SEEN = "SEEN";

    private static final String SQL_CREATE =
            "CREATE TABLE " + HISTORY_TABLE_NAME + " (" +
                    COLUMN_ID + " char(64), " +
                    COLUMN_DATA + " text," +
                    COLUMN_SEEN + " integer"
                    + "," +
                    "PRIMARY KEY (" + COLUMN_ID + "));";

    private static final String SQL_CREATE_INDEX =
            "CREATE UNIQUE INDEX " + HISTORY_TABLE_INDEX + " ON " + " " + HISTORY_TABLE_NAME + "(" + COLUMN_ID + ");";

    private static final String SQL_DELETE_ALL =
            "DELETE FROM " + HISTORY_TABLE_NAME;

    private static final String SQL_QUERY_BY_ID =
            "SELECT \"" + COLUMN_DATA + "\"" +
                    " FROM \"" + HISTORY_TABLE_NAME + "\"" +
                    " WHERE \"" + COLUMN_ID + "\"=?";

    private static final String SQL_QUERY_ALL =
            "SELECT \"" + COLUMN_DATA + "\"" +
                    " FROM \"" + HISTORY_TABLE_NAME + "\" ORDER BY " + COLUMN_ID + " DESC ;";

    private final SQLiteStatement queryById;

    public HistoryDBHelper(Context context) {
        super(context, HISTORY_DATABASE_NAME, null, DATABASE_VERSION);
        queryById = getReadableDatabase().compileStatement(SQL_QUERY_BY_ID);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_INDEX);
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
            return null;
        }
        HistoryEntry he = new GsonBuilder().registerTypeAdapter(HistoryEntry.class, new HistoryEntry.Deserializer()).create().fromJson(data, HistoryEntry.class);
        return he;
    }

    public void put(DetailedEntry entry) {
        HistoryEntry he = new HistoryEntry(entry);
        SQLiteDatabase db = getWritableDatabase();
        db.delete(HISTORY_TABLE_NAME, COLUMN_ID + "=?", new String[] {he.getId()});
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, he.getId());
        values.put(COLUMN_DATA, he.getJson());
        values.put(COLUMN_SEEN, System.currentTimeMillis() / 1000);
        db.replace(HISTORY_TABLE_NAME, null, values);
    }

    public ArrayList<DetailedEntry> queryAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(HISTORY_TABLE_NAME, new String[]{COLUMN_DATA}, null, null, null, null, COLUMN_SEEN + " DESC");
        ArrayList<DetailedEntry> arr = new ArrayList<>(c.getCount());
        if(c.getCount() == 0) return arr;
        c.moveToFirst();
        do {
            arr.add(HistoryEntry.fromJson(c.getString(0)).data);
        } while (c.moveToNext());

        c.close();

        return arr;
    }

    public Cursor getCursor() {
        return getReadableDatabase().rawQuery(SQL_QUERY_ALL, null);
    }

    public void deleteAll() {
        getWritableDatabase().execSQL(SQL_DELETE_ALL);
    }

}
