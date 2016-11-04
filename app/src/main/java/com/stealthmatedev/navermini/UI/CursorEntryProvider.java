package com.stealthmatedev.navermini.UI;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.UI.generic.EntryProvider;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryEntry;

import static android.R.attr.data;
import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/04 0004.
 */

public class CursorEntryProvider extends EntryProvider {

    private Cursor cursor;

    public CursorEntryProvider(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public Entry getEntry(int position) {
        this.cursor.moveToPosition(position);
        return HistoryEntry.fromJson(this.cursor.getString(0)).data;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
}
