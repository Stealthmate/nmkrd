package com.stealthmatedev.navermini.UI.specific.jp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.state.ResultListQuery;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class JpKanjiEntryVisualizer extends EntryVisualizer {

    public JpKanjiEntryVisualizer() {

    }

    public JpKanjiEntryVisualizer(ResultListQuery query) {

    }

    @Override
    public View visualize(Entry entry, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_jp_kanji, parent, false);

        JpKanji kanji = (JpKanji) entry;

        TextView ji = (TextView) view.findViewById(R.id.jp_kanji_ji);
        ji.setText(kanji.kanji.toString());

        TextView radical = (TextView) view.findViewById(R.id.view_jp_entry_kanji_radical);
        radical.setText(kanji.radical.toString());

        TextView strokes = (TextView) view.findViewById(R.id.view_jp_entry_kanji_strokes);
        strokes.setText("" + kanji.strokes);

        TextView kunyomi = (TextView) view.findViewById(R.id.view_jp_entry_kanji_kunyomi);
        String kun = "";
        if (kanji.kunyomi.size() > 0) kun = kanji.kunyomi.get(0);
        for (int i = 1; i <= kanji.kunyomi.size() - 1; i++) {
            kun += " ; " + kanji.kunyomi.get(i);
        }
        kunyomi.setText(kun);

        TextView onyomi = (TextView) view.findViewById(R.id.view_jp_entry_kanji_onyomi);
        String on = "";
        if (kanji.onyomi.size() > 0) on = kanji.onyomi.get(0);
        for (int i = 1; i <= kanji.onyomi.size() - 1; i++) {
            on += " ; " + kanji.onyomi.get(i);
        }
        onyomi.setText(on);

        TextView meanings = (TextView) view.findViewById(R.id.view_jp_entry_kanji_meaning);
        String meaningstr = "";
        ArrayList<JpKanji.Meaning> meaningsarr = kanji.meanings;
        if (meaningsarr.size() > 0) meaningstr = meaningsarr.get(0).m;
        for (int i = 1; i <= meaningsarr.size() - 1; i++) {
            meaningstr += " ; " + meaningsarr.get(i).m;
        }
        meanings.setText(meaningstr);


        return view;
    }
}
