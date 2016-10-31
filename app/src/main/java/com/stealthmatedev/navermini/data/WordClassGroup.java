package com.stealthmatedev.navermini.data;

import com.stealthmatedev.navermini.data.jp.JpWord;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public class WordClassGroup implements Serializable {

    public class Meaning implements Serializable {

        public final String m;

        public Meaning() {
            this.m = "";
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Meaning mn = (Meaning) o;

            if (!m.equals(mn.m)) return false;

            return true;
        }
    }

    public final String wclass;
    public final ArrayList<Meaning> meanings;

    public WordClassGroup() {
        this.wclass = "";
        this.meanings = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordClassGroup wcg = (WordClassGroup) o;

        if (!wcg.wclass.equals(wcg.wclass)) return false;

        if (meanings.size() != wcg.meanings.size()) return false;

        for (int i = 0; i <= meanings.size() - 1; i++) {
            if (!meanings.get(i).equals(wcg.meanings.get(i))) return false;
        }

        return true;
    }

}
