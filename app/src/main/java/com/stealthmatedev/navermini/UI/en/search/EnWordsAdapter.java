package com.stealthmatedev.navermini.UI.en.search;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.data.en.EnWordEntry;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.name;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public class EnWordsAdapter extends ResultListAdapter {
    public EnWordsAdapter(StateManager state, ResultListQuery query, String response) {
        super(state, query, response);
    }

    public EnWordsAdapter(StateManager state, Serializable data) {
        super(state, data);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> wordlist = null;
        try {
            JSONArray wordarr = new JSONArray(result);
            wordlist = new ArrayList<>(wordarr.length());
            for (int i = 0; i <= wordarr.length() - 1; i++) {
                JSONObject item = wordarr.getJSONObject(i);
                wordlist.add(EnWordEntry.fromJSON(item));
            }
        } catch (JSONException e) {
            System.err.println("JSON ERROR");
            e.printStackTrace();
        }

        return wordlist;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.findViewById(R.id.jp_word_name) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_en_word, parent, false);
        }

        EnWordEntry word = (EnWordEntry) getItem(position);
        Resources res = getContext().getResources();


        TextView name = (TextView) convertView.findViewById(R.id.en_word_name);
        name.setText(word.name);

        TextView extra = (TextView) convertView.findViewById(R.id.en_word_extra);
        if(word.getExtra() != null && word.getExtra().length() > 0) extra.setText(String.format(res.getString(R.string.square_brackets), word.getExtra()));
        else extra.setText("");

        TextView wordclass = (TextView) convertView.findViewById(R.id.en_word_class);
        String classes = Arrays.toString(word.wordclasses);
        if (classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) convertView.findViewById(R.id.en_word_meaning);
        meaning.setText(word.meaning);

        return convertView;
    }

    @Override
    protected Class<? extends DetailsVisualizer> getDetailsVisualizerClass(DetailedItem item) {
        return null;
    }
}
