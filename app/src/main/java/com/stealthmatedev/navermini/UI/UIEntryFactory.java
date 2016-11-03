package com.stealthmatedev.navermini.UI;

import com.stealthmatedev.navermini.UI.specific.kr.KrWordUIEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;
import com.stealthmatedev.navermini.state.ResultListQuery;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public enum UIEntryFactory {

    KR_WORD(KrWord.class, KrWordUIEntry.class);

    public final Class<? extends Entry> dataClass;
    public final Class<? extends UIEntry> uiClass;

    UIEntryFactory(Class<? extends Entry> dataClass, Class<? extends UIEntry> uiClass) {
        this.dataClass =dataClass;
        this.uiClass = uiClass;
    }

    public static UIEntry fromEntry(Entry entry) {
        if(entry.getClass().equals(KrWord.class)) return new KrWordUIEntry(entry);
        return null;
    }
}
