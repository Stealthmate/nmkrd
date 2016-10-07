package com.stealthmateoriginal.navermini.UI.jp.worddetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.jp.JpWordDetailsAdapter;
import com.stealthmateoriginal.navermini.data.jp.worddetails.WordDetails;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/03 0003.
 */

public class WordclassAdapter extends ArrayAdapter<String> {

    private WordDetails details;
    private JpWordDetailsAdapter detailsPanel;

    public WordclassAdapter(Context context, JpWordDetailsAdapter detailsPanel, WordDetails details) {
        super(context, 0, new ArrayList<>(details.getWordclasses()));
        this.detailsPanel = detailsPanel;
        this.details = details;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_jp_detail_definition, parent, false);
        }

        final String wordclass = getItem(position);

        TextView wordclassView = (TextView) convertView.findViewById(R.id.view_jp_detail_word_definition_wordclass);
        wordclassView.setText(wordclass);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_jp_detail_word_definition_meaninglist);

        meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_furigana, details.getCompactMeaningsForWordclass(wordclass)));
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                detailsPanel.setDefinition(details.getMeaningsForWordclass(wordclass).get(position));
            }
        });

        return convertView;
    }

}
