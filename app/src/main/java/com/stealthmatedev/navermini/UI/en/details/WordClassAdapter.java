package com.stealthmatedev.navermini.UI.en.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.en.worddetails.Definition;
import com.stealthmatedev.navermini.data.en.worddetails.WordClassGroup;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class WordClassAdapter extends ArrayAdapter<EnWord.WordClassGroup> {
    public WordClassAdapter(Context context, ArrayList<EnWord.WordClassGroup> groups) {
        super(context, 0, groups);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_wclass, parent, false);
        }

        final EnWord.WordClassGroup item = getItem(position);

        if(item == null) return convertView;

        final String wordclass = item.wclass;

        TextView wordclassView = (TextView) convertView.findViewById(R.id.view_listitem_wclass_class);
        wordclassView.setText(wordclass);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_listitem_wclass_deflist);

        ArrayList<String> defstrs = new ArrayList<>(item.meanings.size());
        for(EnWord.WordClassGroup.Meaning mean : item.meanings) defstrs.add(mean.m);


        meanings.setAdapter(new CustomizableArrayAdapter<>(getContext(), R.layout.view_listitem_minimal, defstrs));
        ((CustomizableArrayAdapter)meanings.getAdapter()).setViewStyler(new CustomizableArrayAdapter.ViewStyler() {
            @Override
            public void style(View v) {
                TextView tv = (TextView) v;
                tv.setTextIsSelectable(true);
                tv.setMaxLines(Integer.MAX_VALUE);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.nm_colorText));
            }
        });
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnWordDetailsVisualizer.setDefinition(getContext(), parent.getRootView(), item.meanings.get(position));
            }
        });

        return convertView;


    }
}
