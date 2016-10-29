package com.stealthmatedev.navermini.UI.kr.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.UI.kr.details.KrDetailsVisualizer;
import com.stealthmatedev.navermini.data.kr.KrWordDetails;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.StateManager;
import com.stealthmatedev.navermini.data.kr.KrWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class KrWordsAdapter extends ResultListAdapter {

    private JSONObject json;

    public KrWordsAdapter(StateManager state, String query, String result) {
        super(state, query, result);
    }

    public KrWordsAdapter(StateManager state, SerializableRepresentation repr) {
        super(state, repr);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> wordlist = null;
        try {
            this.json = new JSONObject(result);
            JSONArray wordarr = json.getJSONArray("defs");
            wordlist = new ArrayList<>(wordarr.length());
            for (int i = 0; i <= wordarr.length() - 1; i++) {
                wordlist.add(KrWordEntry.fromJSON(wordarr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            System.err.println("JSON ERROR");
            e.printStackTrace();
        }

        return wordlist;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final KrWordEntry word = (KrWordEntry) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null || convertView.findViewById(R.id.kr_word_name) == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_kr_word, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.kr_word_name);
        name.setText(word.getName());

        TextView hanja = (TextView) convertView.findViewById(R.id.kr_word_hanja);
        System.out.println(convertView);
        hanja.setText(word.getHanja());

        TextView pronun = (TextView) convertView.findViewById(R.id.kr_word_pronun);
        pronun.setText(word.getPronunciation());

        TextView wordclass = (TextView) convertView.findViewById(R.id.kr_word_class);
        String classes = Arrays.toString(word.getWordClasses());
        if(classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) convertView.findViewById(R.id.kr_word_meaning);
        meaning.setText(word.getMeaning());

        return convertView;
    }

    @Override
    protected Class<? extends DetailsVisualizer> getDetailsVisualizerClass(DetailedItem item) {
        return KrDetailsVisualizer.class;
    }
}
