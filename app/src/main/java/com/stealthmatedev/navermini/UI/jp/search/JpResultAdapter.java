package com.stealthmatedev.navermini.UI.jp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.UI.jp.details.JpKanjiDetailsVisualizer;
import com.stealthmatedev.navermini.UI.jp.details.JpWordDetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/28 0028.
 */

public class JpResultAdapter extends ResultListAdapter {

    public JpResultAdapter(StateManager state, ResultListQuery query, String result) {
        super(state, query, result);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DetailedItem.class, new JpWordKanjiDeserializer()).create();
        DetailedItem[] wordlist = gson.fromJson(result, DetailedItem[].class);

        return new ArrayList<>(Arrays.asList(wordlist));
    }

    private View generateKanjiEntry(JpKanji kanji, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.findViewById(R.id.jp_kanji_ji) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_kanji, parent, false);
        }

        TextView ji = (TextView) convertView.findViewById(R.id.jp_kanji_ji);
        ji.setText(kanji.kanji.toString());

        TextView radical = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_radical);
        radical.setText(kanji.radical.toString());

        TextView strokes = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_strokes);
        strokes.setText("" + kanji.strokes);

        TextView kunyomi = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_kunyomi);
        String kun = "";
        if (kanji.kunyomi.size() > 0) kun = kanji.kunyomi.get(0);
        for (int i = 1; i <= kanji.kunyomi.size() - 1; i++) {
            kun += " ; " + kanji.kunyomi.get(i);
        }
        kunyomi.setText(kun);

        TextView onyomi = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_onyomi);
        String on = "";
        if (kanji.onyomi.size() > 0) on = kanji.onyomi.get(0);
        for (int i = 1; i <= kanji.onyomi.size() - 1; i++) {
            on += " ; " + kanji.onyomi.get(i);
        }
        onyomi.setText(on);

        TextView meanings = (TextView) convertView.findViewById(R.id.view_jp_entry_kanji_meaning);
        String meaningstr = "";
        ArrayList<JpKanji.Meaning> meaningsarr = kanji.meanings;
        if (meaningsarr.size() > 0) meaningstr = meaningsarr.get(0).m;
        for (int i = 1; i <= meaningsarr.size() - 1; i++) {
            meaningstr += " ; " + meaningsarr.get(i).m;
        }
        meanings.setText(meaningstr);


        return convertView;
    }

    private View generateWord(JpWord word, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.findViewById(R.id.jp_word_name) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_word, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.jp_word_name);
        name.setText(word.word);

        TextView kanji = (TextView) convertView.findViewById(R.id.jp_word_kanji);
        kanji.setText(word.kanji);

        TextView wordclass = (TextView) convertView.findViewById(R.id.jp_word_class);
        wordclass.setText(word.clsgrps.get(0).wclass);

        TextView meaning = (TextView) convertView.findViewById(R.id.jp_word_meaning);
        meaning.setText(word.clsgrps.get(0).meanings.get(0).glosses.get(0).g);
        return convertView;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {

        Object item = getItem(position);
        if (item instanceof JpWord)
            return generateWord((JpWord) item, convertView, parent);
        else if (item instanceof JpKanji)
            return generateKanjiEntry((JpKanji) item, convertView, parent);

        throw new NullPointerException("Invalid object");
    }

    @Override
    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        if(item instanceof JpWord) return new JpWordDetailsVisualizer((JpWord) item);
        else if (item instanceof JpKanji) return new JpKanjiDetailsVisualizer();
        throw new RuntimeException("Invalid item class in JpResultAdapter: " + item.getClass().getName());
    }

    @Override
    public Serializable getDataRepresentation() {
        return null;
    }

}
