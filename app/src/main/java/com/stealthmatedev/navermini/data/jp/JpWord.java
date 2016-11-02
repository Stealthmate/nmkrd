package com.stealthmatedev.navermini.data.jp;

import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.state.DetailsDictionary;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class JpWord implements Serializable, DetailedItem {

    public static class WordClassGroup implements Serializable {

        public static class Meaning implements Serializable {

            public static class Gloss implements Serializable {

                public final String g;
                public final ArrayList<TranslatedExample> ex;

                public Gloss() {
                    this.g = "";
                    this.ex = new ArrayList<>();
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;

                    Gloss gl = (Gloss) o;

                    if (!g.equals(gl.g)) return false;
                    if (ex.size() != gl.ex.size()) return false;

                    for (int i = 0; i <= ex.size() - 1; i++) {
                        if (!ex.get(i).equals(gl.ex.get(i))) return false;
                    }

                    return true;
                }

            }

            public final String m;
            public final ArrayList<Gloss> glosses;

            public Meaning() {
                this.m = "";
                this.glosses = new ArrayList<>();
            }

            @Override
            public boolean equals(Object o) {

                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Meaning mn = (Meaning) o;

                if (!m.equals(mn.m)) return false;
                if (glosses.size() != mn.glosses.size()) return false;

                for (int i = 0; i <= glosses.size() - 1; i++) {
                    if (!glosses.get(i).equals(mn.glosses.get(i))) return false;
                }

                return true;
            }
        }

        public final String wclass;
        public ArrayList<Meaning> meanings;

        public WordClassGroup() {
            this.wclass = "";
            this.meanings = new ArrayList<>();
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WordClassGroup wcg = (WordClassGroup) o;

            if (!wclass.equals(wcg.wclass)) return false;
            if (meanings.size() != wcg.meanings.size()) return false;

            for (int i = 0; i <= meanings.size() - 1; i++) {
                if (!meanings.get(i).equals(wcg.meanings.get(i))) return false;
            }

            return true;
        }

    }

    public final String word;
    public final String kanji;
    public final String more;
    public final ArrayList<WordClassGroup> clsgrps;

    public JpWord() {
        this.word = "";
        this.kanji = "";
        this.more = "";
        this.clsgrps = new ArrayList<>();
    }

    @Override
    public boolean hasDetails() {
        return more.length() > 0;
    }

    @Override
    public String getLinkToDetails() {
        if (more.startsWith("http")) return more;

        try {
            return DetailsDictionary.JAPANESE_DETAILS.path + "?lnk=" + URLEncoder.encode(more, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpWord jpWord = (JpWord) o;

        if (!word.equals(jpWord.word)) return false;
        if (!kanji.equals(jpWord.kanji)) return false;
        if (!more.equals(jpWord.more)) return false;

        if (clsgrps.size() != jpWord.clsgrps.size()) return false;

        for (int i = 0; i <= clsgrps.size() - 1; i++) {
            if (!clsgrps.get(i).equals(jpWord.clsgrps.get(i))) return false;
        }

        return true;
    }
}
