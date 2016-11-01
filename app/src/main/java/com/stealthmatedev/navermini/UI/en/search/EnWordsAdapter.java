package com.stealthmatedev.navermini.UI.en.search;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.UI.en.details.EnWordDetailsVisualizer;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public class EnWordsAdapter extends ResultListAdapter {
    public EnWordsAdapter(StateManager state, ResultListQuery query, String response) {
        super(state, query, response);
    }

    public EnWordsAdapter(StateManager state, SerializableRepresentation data) {
        super(state, data);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        EnWord[] words = new Gson().fromJson(result, EnWord[].class);
        return new ArrayList<DetailedItem>(Arrays.asList(words));
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.findViewById(R.id.jp_word_name) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_generic_detail_word, parent, false);
        }

        EnWord word = (EnWord) getItem(position);

        if(word == null) return convertView;

        Resources res = getContext().getResources();


        TextView name = (TextView) convertView.findViewById(R.id.en_word_name);
        name.setText(word.word);

        TextView extra = (TextView) convertView.findViewById(R.id.en_word_extra);
        String extraInfo = word.hanja;
        if(extraInfo.length() == 0) extraInfo = word.pronun;
        if(extraInfo.length() > 0) extra.setText(String.format(res.getString(R.string.square_brackets), extraInfo));
        else extra.setText("");

        TextView wordclass = (TextView) convertView.findViewById(R.id.en_word_class);
        wordclass.setText(word.wclass);

        TextView meaning = (TextView) convertView.findViewById(R.id.en_word_meaning);
        meaning.setText(word.clsgrps.get(0).meanings.get(0).m);

        return convertView;
    }

    @Override
    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        return new EnWordDetailsVisualizer();
    }
}
