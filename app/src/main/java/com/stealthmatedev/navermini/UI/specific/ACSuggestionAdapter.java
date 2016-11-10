package com.stealthmatedev.navermini.UI.specific;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.FilterlessArrayAdapter;
import com.stealthmatedev.navermini.data.AutocompleteSuggestion;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/08 0008.
 */

public class ACSuggestionAdapter extends FilterlessArrayAdapter<AutocompleteSuggestion> {
    public ACSuggestionAdapter(Context context, ArrayList<AutocompleteSuggestion> suggestions) {
        super(context, 0, suggestions);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_autocomplete_suggestion, parent, false);
        }

        AutocompleteSuggestion item = getItem(position);

        TextView word = (TextView) convertView.findViewById(R.id.view_listitem_acsug_word);
        word.setText(item.word);

        TextView meaning = (TextView) convertView.findViewById(R.id.view_listitem_acsug_meaning);
        meaning.setText(item.meaning);

        return convertView;
    }
}
