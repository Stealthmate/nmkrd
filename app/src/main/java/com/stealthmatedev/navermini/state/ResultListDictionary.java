package com.stealthmatedev.navermini.state;

import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.UI.jp.search.JpWordsAdapter;
import com.stealthmatedev.navermini.UI.kr.search.EnWordsAdapter;
import com.stealthmatedev.navermini.UI.kr.search.KrWordsAdapter;
import com.stealthmatedev.navermini.UI.kr.search.KrExamplesAdapter;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public enum ResultListDictionary {
    KOREAN("/kr", new SubDictionary[]{
            new SubDictionary("Words", "", KrWordsAdapter.class),
            new SubDictionary("Examples", "/ex", KrExamplesAdapter.class)}),
    JAPANESE("/jp", new SubDictionary[]{
            new SubDictionary("Words", "", JpWordsAdapter.class)}),
    ENGLISH("/en", new SubDictionary[] {
            new SubDictionary("Words", "", EnWordsAdapter.class));

    public static class SubDictionary {
        public final String name;
        public final String path;
        public final Class<? extends ResultListAdapter> resultAdapter;

        private SubDictionary(String name, String path, Class<? extends ResultListAdapter> resultAdapter) {
            this.name = name;
            this.path = path;
            this.resultAdapter = resultAdapter;
        }
    }

    public final String path;
    public final SubDictionary[] subdicts;

    ResultListDictionary(String path, SubDictionary[] subdicts) {
        this.path = path;
        this.subdicts = subdicts;
    }

    public SubDictionary getSubDict(String subdict) {
        for (int i = 0; i <= subdicts.length - 1; i++) {
            if(subdicts[i].name.equals(subdict)) return subdicts[i];
        }

        return null;
    }
}
