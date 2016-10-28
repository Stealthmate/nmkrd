package com.stealthmatedev.navermini.UI.jp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.StateManager;
import com.stealthmatedev.navermini.data.jp.JpKanjiEntry;
import com.stealthmatedev.navermini.data.jp.JpWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by Stealthmate on 16/09/28 0028.
 */

public class JpWordsAdapter extends ResultListAdapter {

    private static final String TYPE = "type";
    private static final String KANJI = "k";
    private static final String WORD = "d";

    private static final Pattern RUBY = Pattern.compile("\\(([^\\(\\);]+);([^\\(\\);]+)\\)");

    public JpWordsAdapter(StateManager state, String query, String result) {
        super(state, query, result);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> wordlist = null;
        try {
            JSONArray wordarr = new JSONArray(result);
            wordlist = new ArrayList<>(wordarr.length());
            for (int i = 0; i <= wordarr.length() - 1; i++) {
                JSONObject item = wordarr.getJSONObject(i);
                String type = item.getString(TYPE);
                if (type.equals(KANJI)) wordlist.add(JpKanjiEntry.fromJSON(item));
                else if (type.equals(WORD)) wordlist.add(JpWordEntry.fromJSON(item));
                else throw new JSONException("Invalid JSON obj type");
            }
        } catch (JSONException e) {
            System.err.println("JSON ERROR");
            e.printStackTrace();
        }

        return wordlist;
    }

    private View generateKanjiEntry(JpKanjiEntry kanji, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.findViewById(R.id.jp_kanji_ji) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_kanji, parent, false);
        }

        TextView ji = (TextView) convertView.findViewById(R.id.jp_kanji_ji);
        ji.setText(kanji.getKanji());

        TextView radical = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_radical);
        radical.setText(kanji.getRadical());

        TextView strokes = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_strokes);
        strokes.setText("" + kanji.getStrokes());

        TextView kunyomi = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_kunyomi);
        String kun = "";
        if (kanji.getKunyomi().length > 0) kun = kanji.getKunyomi()[0];
        for (int i = 1; i <= kanji.getKunyomi().length - 1; i++) {
            kun += " ; " + kanji.getKunyomi()[i];
        }
        kunyomi.setText(kun);

        TextView onyomi = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_onyomi);
        String on = "";
        if (kanji.getOnyomi().length > 0) on = kanji.getOnyomi()[0];
        for (int i = 1; i <= kanji.getOnyomi().length - 1; i++) {
            on += " ; " + kanji.getOnyomi()[i];
        }
        onyomi.setText(on);

        TextView meanings = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_meaning);
        String meaningstr = "";
        String[] meaningsarr = kanji.getMeanings();
        if (meaningsarr.length > 0) meaningstr = meaningsarr[0];
        for (int i = 1; i <= meaningsarr.length - 1; i++) {
            meaningstr += " ; " + meaningsarr[i];
        }
        meanings.setText(meaningstr);


        return convertView;
    }

    private View generateWordEntry(JpWordEntry word, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.findViewById(R.id.jp_word_name) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_word, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.jp_word_name);
        name.setText(word.getName());

        TextView kanji = (TextView) convertView.findViewById(R.id.jp_word_kanji);
        kanji.setText(word.getKanji());

        TextView wordclass = (TextView) convertView.findViewById(R.id.jp_word_class);
        String classes = Arrays.toString(word.getWordClasses());
        if (classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) convertView.findViewById(R.id.jp_word_meaning);
        String meaningStr = word.getMeaning();

        meaning.setText(meaningStr);
        return convertView;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {

        Object item = getItem(position);
        if (item instanceof JpWordEntry)
            return generateWordEntry((JpWordEntry) item, convertView, parent);
        else if (item instanceof JpKanjiEntry)
            return generateKanjiEntry((JpKanjiEntry) item, convertView, parent);

        throw new NullPointerException("Invalid object");
    }

    @Override
    public Serializable getDataRepresentation() {
        return null;
    }

}
