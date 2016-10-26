package com.stealthmateoriginal.navermini.UI.jp.details.kanji;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmateoriginal.navermini.UI.generic.FixedListView;
import com.stealthmateoriginal.navermini.data.jp.kanjidetails.KanjiMeaning;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/21 0021.
 */

class KanjiMeaningsAdapter extends ArrayAdapter<KanjiMeaning> {

    private static class ExAdapter extends CustomizableArrayAdapter<String> {

        public ExAdapter(Context context, int resource, ArrayList<Pair<String, String>> items) {
            super(context, resource);
            for(Pair<String, String> ex : items) {
                this.add(ex.second + " - " + ex.first);
            }
        }
    }

    public KanjiMeaningsAdapter(Context context, ArrayList<KanjiMeaning> meanings) {
        super(context, 0, meanings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        KanjiMeaning m = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_jp_kanji_listitem_meanings, parent, false);

        TextView meaning = (TextView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_meaning);
        meaning.setText(m.meaning);

        FixedListView ex = (FixedListView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_ex);
        ex.setAdapter(new ExAdapter(getContext(), R.layout.view_listitem_furigana, m.ex));

        return convertView;
    }
}
