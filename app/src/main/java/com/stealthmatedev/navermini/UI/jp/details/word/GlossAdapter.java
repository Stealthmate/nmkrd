package com.stealthmatedev.navermini.UI.jp.details.word;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.UI.TranslatedExampleAdapter;
import com.stealthmatedev.navermini.data.jp.worddetails.Gloss;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class GlossAdapter extends ArrayAdapter<Gloss> {
    public GlossAdapter(Context context, ArrayList<Gloss> glosses) {
        super(context, 0, glosses);
    }

    @Override
    public int getCount() {
        if(super.getCount() > 1) return super.getCount() - 1;
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if(super.getCount() > 1) position = position + 1;

        if(convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_detail_definition_meaning_gloss, parent, false);

        TextView gloss = (TextView) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss);
        gloss.setText(getItem(position).getGloss());
        if(gloss.getText().length() == 0) gloss.setVisibility(GONE);
        else gloss.setVisibility(View.VISIBLE);

        ListLayout ex = (ListLayout) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss_ex);
        ex.clear();
        ex.populate(new TranslatedExampleAdapter(getContext(), getItem(position).getExamples()));

        return convertView;
    }
}
