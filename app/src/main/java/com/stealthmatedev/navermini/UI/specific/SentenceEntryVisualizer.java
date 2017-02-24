package com.stealthmatedev.navermini.UI.specific;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.SentenceEntry;

/**
 * Created by Stealthmate on 17/02/24 0024.
 */

public class SentenceEntryVisualizer extends EntryVisualizer {
    @Override
    public View visualize(Entry entry, ViewGroup parent) {
        SentenceEntry item = (SentenceEntry) entry;

        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_sentence, parent, false);

        TextView cc = (TextView) convertView.findViewById(R.id.view_listitem_sentence_cc);
        cc.setText(item.from.code + " - " + item.to.code);

        TextView keyword = (TextView) convertView.findViewById(R.id.view_listitem_sentence_keyword);
        keyword.setText(item.keyword);

        TextView original = (TextView) convertView.findViewById(R.id.view_listitem_sentence_original);
        original.setText(item.ex);

        TextView translated = (TextView) convertView.findViewById(R.id.view_listitem_sentence_translated);
        if (item.tr.length() > 0) {
            translated.setText(item.tr);
            translated.setVisibility(View.VISIBLE);
        } else {
            translated.setVisibility(View.GONE);
        }

        return convertView;
    }
}
