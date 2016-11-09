package com.stealthmatedev.navermini.serverapi;

import android.content.Context;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.en.EnAutocompleter;
import com.stealthmatedev.navermini.data.kr.KrAutocompleter;
import com.stealthmatedev.navermini.serverapi.en.EnWordResponseTranslator;
import com.stealthmatedev.navermini.serverapi.jp.JpWordKanjiResponseTranslator;
import com.stealthmatedev.navermini.serverapi.kr.KrExampleResponseTranslator;
import com.stealthmatedev.navermini.serverapi.kr.KrWordResponseTranslator;
import com.stealthmatedev.navermini.state.Autocompleter;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public enum EntryListDictionary {
    KOREAN("/kr", new SubDictionary[]{
            new SubDictionary("KOREAN", R.string.subdict_words, "", new KrWordResponseTranslator()),
            new SubDictionary("KOREAN", R.string.subdict_ex, "/ex", new KrExampleResponseTranslator())}, new KrAutocompleter()),
    JAPANESE("/jp", new SubDictionary[]{
            new SubDictionary("JAPANESE", R.string.subdict_words, "", new JpWordKanjiResponseTranslator())}, null),
    ENGLISH("/en", new SubDictionary[]{
            new SubDictionary("ENGLISH", R.string.subdict_words, "", new EnWordResponseTranslator())}, new EnAutocompleter());

    public static class SubDictionary {

        public final String parent;
        public final int name;
        public final String path;
        public final ResponseTranslator translator;
        private SubDictionary(String parent, int name, String path, ResponseTranslator translator) {
            this.parent = parent;
            this.name = name;
            this.path = path;
            this.translator = translator;
        }
    }

    public final String path;
    public final SubDictionary[] subdicts;
    public final Autocompleter autocompleter;


    EntryListDictionary(String path, SubDictionary[] subdicts, Autocompleter autocompleter) {
        this.path = path;
        this.subdicts = subdicts;
        this.autocompleter = autocompleter;
    }

    public SubDictionary getSubDict(Context context, String subdict) {
        for (int i = 0; i <= subdicts.length - 1; i++) {
            String dictname = context.getResources().getString(subdicts[i].name);
            if (dictname.equals(subdict)) return subdicts[i];
        }

        return null;
    }

    public int indexOf(SubDictionary subdict) {
        for (int i = 0; i <= subdicts.length - 1; i++) {
            if (subdicts[i].equals(subdict)) return i;
        }
        return -1;
    }
}
