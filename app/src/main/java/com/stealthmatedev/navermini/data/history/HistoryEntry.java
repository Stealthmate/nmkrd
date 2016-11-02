package com.stealthmatedev.navermini.data.history;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.kr.KrWord;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public class HistoryEntry {

    private enum Type {
        KR_WORD(0, KrWord.class),
        JP_WORD(1, JpWord.class),
        JP_KANJI(2, JpKanji.class),
        EN_WORD(3, EnWord.class);

        private final int id;

        private final Class<? extends DetailedItem> classType;

        Type(int id, Class<? extends DetailedItem> classType) {
            this.id = id;
            this.classType = classType;
        }

        public static Type fromItem(DetailedItem item) {
            Class<? extends DetailedItem> cls = item.getClass();

            if(cls.equals(KrWord.class)) return KR_WORD;
            if(cls.equals(JpWord.class)) return JP_WORD;
            if(cls.equals(JpKanji.class)) return JP_KANJI;
            if(cls.equals(EnWord.class)) return EN_WORD;

            return null;
        }
    }

    public final String version_id;
    public final Type type;
    public final DetailedItem data;

    public HistoryEntry() {
        this.version_id = "";
        this.type = null;
        this.data = null;
    }

    public HistoryEntry(DetailedItem item) {
        this.version_id = "1";
        this.type = Type.fromItem(item);
        this.data = item;

    }

    public String getJson() {
        return new Gson().toJson(this);
    }

}
