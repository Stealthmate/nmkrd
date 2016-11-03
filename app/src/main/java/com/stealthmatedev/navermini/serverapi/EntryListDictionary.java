package com.stealthmatedev.navermini.serverapi;

import com.stealthmatedev.navermini.UI.NetworkEntryListAdapter;
import com.stealthmatedev.navermini.serverapi.en.EnWordResponseTranslator;
import com.stealthmatedev.navermini.serverapi.jp.JpWordKanjiResponseTranslator;
import com.stealthmatedev.navermini.serverapi.kr.KrExampleResponseTranslator;
import com.stealthmatedev.navermini.serverapi.kr.KrWordResponseTranslator;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public enum EntryListDictionary {
    KOREAN("/kr", new SubDictionary[]{
            new SubDictionary("Words", "", new KrWordResponseTranslator()),
            new SubDictionary("Examples", "/ex", new KrExampleResponseTranslator())}),
    JAPANESE("/jp", new SubDictionary[]{
            new SubDictionary("Words", "", new JpWordKanjiResponseTranslator())}),
    ENGLISH("/en", new SubDictionary[]{
            new SubDictionary("Words", "", new EnWordResponseTranslator())});

    public static class SubDictionary {

        public final String name;
        public final String path;
        public final Class<? extends NetworkEntryListAdapter> resultAdapter;
        public final ResponseTranslator translator;

        private SubDictionary(String name, String path, ResponseTranslator translator) {
            this.name = name;
            this.path = path;
            this.resultAdapter = null;
            this.translator = translator;
        }
    }

    public final String path;
    public final SubDictionary[] subdicts;

    EntryListDictionary(String path, SubDictionary[] subdicts) {
        this.path = path;
        this.subdicts = subdicts;
    }

    public SubDictionary getSubDict(String subdict) {
        for (int i = 0; i <= subdicts.length - 1; i++) {
            if (subdicts[i].name.equals(subdict)) return subdicts[i];
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
