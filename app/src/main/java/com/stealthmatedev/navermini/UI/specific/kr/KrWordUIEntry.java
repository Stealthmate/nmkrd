package com.stealthmatedev.navermini.UI.specific.kr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.UIEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class KrWordUIEntry extends UIEntry {

    public KrWordUIEntry(Entry data) {
        super(data);
    }

    @Override
    public View getView(ViewGroup parent) {
        final KrWord word = (KrWord) data;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kr_word, parent, false);

        TextView name = (TextView) view.findViewById(R.id.kr_word_name);
        name.setText(word.word);

        TextView hanja = (TextView) view.findViewById(R.id.kr_word_hanja);
        hanja.setText(word.hanja);

        TextView pronun = (TextView) view.findViewById(R.id.kr_word_pronun);
        pronun.setText(word.pronun);

        TextView wordclass = (TextView) view.findViewById(R.id.kr_word_class);
        String classes = word.wclass;
        if (classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) view.findViewById(R.id.kr_word_meaning);
        meaning.setText(word.defs.get(0).def);

        return view;
    }
}
