package com.stealthmatedev.navermini.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stealthmatedev.navermini.data.sentencestore.SentenceStore;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class DBHelper extends SQLiteOpenHelper{

    public interface TableManager {
        void onCreate(SQLiteDatabase db);
        void onUpdate();
    }

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NMKRD_DB";

    public final SentenceStore sentenceStore;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.sentenceStore = new SentenceStore(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sentenceStore.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sentenceStore.onUpdate();
    }
}
