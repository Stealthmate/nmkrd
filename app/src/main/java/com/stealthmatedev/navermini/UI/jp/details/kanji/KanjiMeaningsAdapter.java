package com.stealthmatedev.navermini.UI.jp.details.kanji;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.UI.generic.FixedListView;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.kanjidetails.KanjiMeaning;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/21 0021.
 */

class KanjiMeaningsAdapter extends ArrayAdapter<JpKanji.Meaning> {

    private static class ExAdapter extends CustomizableArrayAdapter<String> {

        public ExAdapter(Context context, int resource, ArrayList<TranslatedExample> items) {
            super(context, resource);
            for (TranslatedExample ex : items) {
                this.add(ex.ex + " - " + ex.tr);
            }
        }
    }

    public KanjiMeaningsAdapter(Context context, ArrayList<JpKanji.Meaning> meanings) {
        super(context, 0, meanings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JpKanji.Meaning m = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_jp_kanji_listitem_meanings, parent, false);

        TextView meaning = (TextView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_meaning);
        meaning.setText(m.m);

        FixedListView ex = (FixedListView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_ex);
        ex.setAdapter(new ExAdapter(getContext(), R.layout.view_listitem_furigana, m.ex));

        return convertView;
    }
}
