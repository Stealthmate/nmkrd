package com.stealthmatedev.navermini.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stealthmatedev.navermini.data.history.HistoryTableManager;
import com.stealthmatedev.navermini.data.sentencestore.SentenceStoreTableManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class DBHelper extends SQLiteOpenHelper{

    public static abstract class TableManager {

        public interface Observer {
            void onChanged();
        }

        private Set<Observer> observers;

        protected final DBHelper helper;

        public TableManager(DBHelper helper) {
            this.observers = new HashSet<>();
            this.helper = helper;
        }

        public final void registerObserver(Observer obs) {
            this.observers.add(obs);
        }

        public final void unregisterObserver(Observer obs) {
            this.observers.remove(obs);
        }

        protected final void notifyChanged() {
            for(Observer obs : observers) obs.onChanged();
        }

        public abstract void onCreate(SQLiteDatabase db);
        public abstract void onUpdate();
    }

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NMKRD_DB";

    private static final int T_HISTORY = 0;
    private static final int T_SENTENCE_STORE = 1;

    private HashMap<Integer, TableManager> tables;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.tables = new HashMap<>();
        this.tables.put(T_HISTORY, new HistoryTableManager(this));
        this.tables.put(T_SENTENCE_STORE, new SentenceStoreTableManager(this));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Map.Entry<Integer, TableManager>  e: tables.entrySet()) e.getValue().onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(Map.Entry<Integer, TableManager>  e: tables.entrySet()) e.getValue().onUpdate();
    }

    public HistoryTableManager history() {
        return (HistoryTableManager) tables.get(T_HISTORY);
    }

    public SentenceStoreTableManager sentenceStore() {
        return (SentenceStoreTableManager) tables.get(T_SENTENCE_STORE);
    }
}
