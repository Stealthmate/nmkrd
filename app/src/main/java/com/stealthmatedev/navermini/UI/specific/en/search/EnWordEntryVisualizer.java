package com.stealthmatedev.navermini.UI.specific.en.search;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.en.EnWord;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class EnWordEntryVisualizer extends EntryVisualizer {
    @Override
    public View visualize(Entry entry, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_generic_detail_word_head, parent, false);

        EnWord word = (EnWord) entry;

        if(word == null) return view;

        Resources res = parent.getContext().getResources();

        TextView name = (TextView) view.findViewById(R.id.en_word_name);
        name.setText(word.word);

        TextView extra = (TextView) view.findViewById(R.id.en_word_extra);
        String extraInfo = word.hanja;
        if(extraInfo.length() == 0) extraInfo = word.pronun;
        if(extraInfo.length() > 0) extra.setText(String.format(res.getString(R.string.square_brackets), extraInfo));
        else extra.setText("");

        TextView wordclass = (TextView) view.findViewById(R.id.en_word_class);
        wordclass.setText(word.wclass);

        TextView meaning = (TextView) view.findViewById(R.id.en_word_meaning);
        meaning.setText(word.clsgrps.get(0).meanings.get(0).m);

        return view;
    }
}
