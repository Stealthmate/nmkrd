package com.stealthmatedev.navermini.UI.specific.jp.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.state.ResultListQuery;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class JpWordEntryVisualizer extends EntryVisualizer {

    String highlight;

    public JpWordEntryVisualizer() {

    }

    public JpWordEntryVisualizer(ResultListQuery query) {
        this.highlight = query.query;
    }

    @Override
    public View visualize(Entry entry, ViewGroup parent) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_jp_word, parent, false);

        JpWord word = (JpWord) entry;

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
}
