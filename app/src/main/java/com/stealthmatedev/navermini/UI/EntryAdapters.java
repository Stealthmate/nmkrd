package com.stealthmatedev.navermini.UI;

import android.util.Log;

import com.stealthmatedev.navermini.UI.specific.kr.KrWordEntryAdapter;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public enum EntryAdapters {
    KR_WORD(KrWord.class, new KrWordEntryAdapter());

    public final Class<? extends Entry> entryType;
    public final EntryAdapter adapter;

    EntryAdapters(Class<? extends Entry> entryType, EntryAdapter adapter) {
        this.entryType = entryType;
        this.adapter = adapter;
    }

    public static EntryAdapter forEntry(Entry entry) {
        Log.i(APPTAG, "ENTRY CLASS " + entry.getClass() + " " + KR_WORD.entryType + String.valueOf(entry.getClass().equals(KR_WORD.entryType)));
        if(entry.getClass().equals(KR_WORD.entryType)) return KR_WORD.adapter;
        return null;
    }
}
