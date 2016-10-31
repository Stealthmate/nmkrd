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
import com.stealthmatedev.navermini.UI.jp.details.kanji.JpKanjiDetailsVisualizer;
import com.stealthmatedev.navermini.UI.jp.details.word.JpWordDetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;
import com.stealthmatedev.navermini.data.jp.JpKanjiEntry;

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

    public JpWordsAdapter(StateManager state, ResultListQuery query, String result) {
        super(state, query, result);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DetailedItem.class, new JpWordKanjiDeserializer()).create();
        DetailedItem[] wordlist = gson.fromJson(result, DetailedItem[].class);

        return new ArrayList<>(Arrays.asList(wordlist));
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
        else if (item instanceof JpKanjiEntry)
            return generateKanjiEntry((JpKanjiEntry) item, convertView, parent);

        throw new NullPointerException("Invalid object");
    }

    @Override
    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        if(item instanceof JpWord) return new JpWordDetailsVisualizer((JpWord) item);
        else if (item instanceof JpKanjiEntry) return new JpKanjiDetailsVisualizer();
        throw new RuntimeException("Invalid item class in JpWordsAdapter: " + item.getClass().getName());
    }

    @Override
    public Serializable getDataRepresentation() {
        return null;
    }

}
