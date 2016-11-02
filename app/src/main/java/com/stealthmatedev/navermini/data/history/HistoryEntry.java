package com.stealthmatedev.navermini.data.history;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public class HistoryEntry {

    private enum Type {
        KR_WORD(0),
        JP_WORD(1),
        JP_KANJI(2),
        EN_WORD(3);

        private final int id;

        Type(int id) {
            this.id = id;
        }
    }

    private final String version_id;
    private final Type type;
    private final HistoryItem data;

    public HistoryEntry() {
        this.version_id = "";
        this.type = null;
        this.data = null;
    }


}
