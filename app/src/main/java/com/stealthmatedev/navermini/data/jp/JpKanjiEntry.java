package com.stealthmatedev.navermini.data.jp;

import com.stealthmatedev.navermini.state.DetailsDictionary;
import com.stealthmatedev.navermini.state.DetailedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/29 0029.
 */

public class JpKanjiEntry implements DetailedItem {

    private static final String KANJI = "ji";
    private static final String ONYOMI = "on";
    private static final String KUNYOMI = "kun";
    private static final String STROKES = "str";
    private static final String RADICAL = "rad";
    private static final String MEANINGS = "mean";
    private static final String MORE = "more";

    private static final String NO_MORE_INFO = "NOMOREINFO";

    private final String ji;
    private final String[] onyomi;
    private final String[] kunyomi;
    private final int strokes;
    private final String radical;
    private final String[] meanings;


    private final String moreInfo;

    public static JpKanjiEntry fromJSON(JSONObject obj) throws JSONException {

        String kanji = obj.getString(KANJI);

        String[] onyomi = null;
        if (obj.has(ONYOMI)) {
            JSONArray arr = obj.getJSONArray(ONYOMI);
            onyomi = new String[arr.length()];
            for (int i = 0; i <= arr.length() - 1; i++) {
                onyomi[i] = arr.getString(i);
            }
        }

        String[] kunyomi = null;
        if (obj.has(KUNYOMI)) {
            JSONArray arr = obj.getJSONArray(KUNYOMI);
            kunyomi = new String[arr.length()];
            for (int i = 0; i <= arr.length() - 1; i++) {
                kunyomi[i] = arr.getString(i);
            }
        }

        int strokes = obj.getInt(STROKES);

        String radical = obj.getString(RADICAL);

        String[] meanings;
        JSONArray meanarr = obj.getJSONArray(MEANINGS);
        meanings = new String[meanarr.length()];
        for (int i = 0; i <= meanarr.length() - 1; i++) {
            meanings[i] = meanarr.getString(i);
        }

        String more = NO_MORE_INFO;
        if(obj.has(MORE)) more = obj.getString(MORE);

        return new JpKanjiEntry(
                kanji,
                onyomi,
                kunyomi,
                strokes,
                radical,
                meanings,
                more
        );
    }

    private JpKanjiEntry(String ji, String[] onyomi, String[] kunyomi, int strokes, String radical, String[] meanings, String moreInfo) {
        this.ji = ji;
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
        this.strokes = strokes;
        this.radical = radical;
        this.meanings = meanings;
        this.moreInfo = moreInfo;
    }


    public String getKanji() {
        return ji;
    }

    public String[] getOnyomi() {
        if(onyomi == null) return new String[0];
        return onyomi;
    }

    public String[] getKunyomi() {
        if(kunyomi == null) return new String[0];
        return kunyomi;
    }

    public int getStrokes() {
        return strokes;
    }

    public String getRadical() {
        return radical;
    }

    public String[] getMeanings() {
        return meanings;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JpKanjiEntry that = (JpKanjiEntry) o;

        if (!ji.equals(that.ji)) return false;

        if (!Arrays.equals(onyomi, that.onyomi)) return false;
        if (!Arrays.equals(kunyomi, that.kunyomi)) return false;

        if (strokes != that.strokes) return false;
        if (!radical.equals(that.radical)) return false;

        if (!Arrays.equals(meanings, that.meanings)) return false;

        if(!moreInfo.equals(that.moreInfo)) return false;

        return true;

    }

    @Override
    public boolean hasDetails() {
        return true;
    }

    @Override
    public String getLinkToDetails() {
        try {
            return DetailsDictionary.JAPANESE_DETAILS.path + "?lnk=" + URLEncoder.encode(moreInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported utf-8 wtf");
            System.exit(-1);
            e.printStackTrace();
        }

        return null;
    }
}
