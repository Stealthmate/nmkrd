package com.stealthmatedev.navermini.data;

import android.support.v4.util.Pair;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public interface SentenceEntry extends Entry {

    enum Language {
        KR("KR"),
        JP("JP"),
        EN("EN");

        public final String code;

        Language(String code) {
            this.code = code;
        }
    }


    String getUntranslated();
    String getTranslated();
    Pair<Language, Language> getLanguages();
}
