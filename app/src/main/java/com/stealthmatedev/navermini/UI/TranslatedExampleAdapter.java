package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.TranslatedExample;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/05 0005.
 */

public class TranslatedExampleAdapter extends ArrayAdapter<TranslatedExample> {
    public TranslatedExampleAdapter(Context context, ArrayList<TranslatedExample> examples) {
        super(context, 0, examples);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_general_ex_translated, parent, false);

        TextView original = (TextView) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss_ex_original);
        original.setText(getItem(position).original);

        TextView translation = (TextView) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss_ex_translation);
        translation.setText(getItem(position).translated);

        return convertView;
    }
}
