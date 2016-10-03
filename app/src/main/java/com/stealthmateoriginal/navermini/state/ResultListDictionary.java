package com.stealthmateoriginal.navermini.state;

import com.stealthmateoriginal.navermini.UI.ResultListAdapter;
import com.stealthmateoriginal.navermini.UI.jp.JpWordsAdapter;
import com.stealthmateoriginal.navermini.UI.kr.KrWordsAdapter;

import java.util.ArrayList;

import javax.xml.transform.Result;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public enum ResultListDictionary {
    KOREAN("/kr", new SubDictionary[]{
            new SubDictionary("Words", "", KrWordsAdapter.class)}),
    JAPANESE("/jp", new SubDictionary[]{
            new SubDictionary("Words", "", JpWordsAdapter.class)});

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
}
