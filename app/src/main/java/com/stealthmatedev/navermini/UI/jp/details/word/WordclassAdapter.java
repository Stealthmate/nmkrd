package com.stealthmatedev.navermini.UI.jp.details.word;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.jp.worddetails.WordDetails;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/03 0003.
 */

public class WordclassAdapter extends ArrayAdapter<String> {

    private WordDetails details;

    public WordclassAdapter(Context context, WordDetails details) {
        super(context, 0, new ArrayList<>(details.getWordclasses()));
        this.details = details;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_wclass, parent, false);
        }

        final String wordclass = getItem(position);

        TextView wordclassView = (TextView) convertView.findViewById(R.id.view_listitem_wclass_class);
        wordclassView.setText(wordclass);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_listitem_wclass_deflist);

        meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_furigana, details.getCompactMeaningsForWordclass(wordclass)));
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JpWordDetailsVisualizer.setDefinition(getContext(), parent.getRootView(), details.getMeaningsForWordclass(wordclass).get(position));
            }
        });

        return convertView;
    }

}
