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
import com.stealthmatedev.navermini.UI.generic.UIUtils;
import com.stealthmatedev.navermini.data.en.EnWord;

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
            public void style(View v, int position) {
                TextView tv = (TextView) v;
                ViewGroup.LayoutParams lp = tv.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                tv.setLayoutParams(lp);
                tv.setMaxLines(Integer.MAX_VALUE);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.nm_colorText));
                Context c = getContext();
                int h_pad = (int) UIUtils.dp(c, 5);
                int v_pad = (int) UIUtils.dp(c, 10);
                tv.setPadding(h_pad, v_pad, h_pad, v_pad);
                tv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selector_primary_light));
                tv.invalidate();
            }
        });
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnWordDetailsVisualizer.setDefinition(parent.getRootView(), item.meanings.get(position));
            }
        });

        return convertView;


    }
}
