package com.stealthmatedev.navermini.UI.specific.kr.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.NetworkEntryListAdapter;
import com.stealthmatedev.navermini.UI.specific.kr.details.KrDetailsVisualizer;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */

public class KrWordVisualizer extends NetworkEntryListAdapter {

    private JSONObject json;

    public KrWordVisualizer(StateManager state, ResultListQuery query, String result) {
        super(null, null, null);
    }

    public KrWordVisualizer(StateManager state, SerializableRepresentation repr) {
        super(null, null, null);
    }

    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> wordlist = null;
        Gson gson = new Gson();
        KrWord[] entries = gson.fromJson(result, KrWord[].class);
        wordlist = new ArrayList<DetailedItem>(Arrays.asList(entries));
        return wordlist;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final KrWord word = (KrWord) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null || convertView.findViewById(R.id.kr_word_name) == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kr_word, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.kr_word_name);
        name.setText(word.word);

        TextView hanja = (TextView) convertView.findViewById(R.id.kr_word_hanja);
        System.out.println(convertView);
        hanja.setText(word.hanja);

        TextView pronun = (TextView) convertView.findViewById(R.id.kr_word_pronun);
        pronun.setText(word.pronun);

        TextView wordclass = (TextView) convertView.findViewById(R.id.kr_word_class);
        String classes = word.wclass;
        if(classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) convertView.findViewById(R.id.kr_word_meaning);
        meaning.setText(word.defs.get(0).def);

        return convertView;
    }

    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        return new KrDetailsVisualizer((KrWord) item);
    }

    protected Class<? extends DetailedItem> getItemClass(DetailedItem item) {
        return KrWord.class;
    }
}
