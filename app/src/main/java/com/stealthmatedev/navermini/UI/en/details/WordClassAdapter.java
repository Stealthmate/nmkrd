package com.stealthmatedev.navermini.UI.en.details;

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
import com.stealthmatedev.navermini.data.en.worddetails.Definition;
import com.stealthmatedev.navermini.data.en.worddetails.WordClassGroup;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class WordClassAdapter extends ArrayAdapter<WordClassGroup> {
    public WordClassAdapter(Context context, ArrayList<WordClassGroup> groups) {
        super(context, 0, groups);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_wclass, parent, false);
        }

        final WordClassGroup item = getItem(position);

        final String wordclass = item.wclass;

        TextView wordclassView = (TextView) convertView.findViewById(R.id.view_listitem_wclass_class);
        wordclassView.setText(wordclass);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_listitem_wclass_deflist);

        ArrayList<String> defstrs = new ArrayList<>(item.defs.size());
        for(Definition def : item.defs) defstrs.add(def.def);

        meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_furigana, defstrs));
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnWordDetailsVisualizer.setDefinition(getContext(), parent.getRootView(), item.defs.get(position));
            }
        });

        return convertView;


    }
}
