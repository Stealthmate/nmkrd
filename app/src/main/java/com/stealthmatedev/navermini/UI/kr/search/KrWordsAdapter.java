package com.stealthmatedev.navermini.UI.kr.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.UI.kr.details.KrDetailsVisualizer;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
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

    public KrWordsAdapter(StateManager state, ResultListQuery query, String result) {
        super(state, query, result);
    }

    public KrWordsAdapter(StateManager state, SerializableRepresentation repr) {
        super(state, repr);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> wordlist = null;
        Gson gson = new Gson();
        KrWordEntry[] entries = gson.fromJson(result, KrWordEntry[].class);
        wordlist = new ArrayList<DetailedItem>(Arrays.asList(entries));
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
        name.setText(word.word);

        TextView hanja = (TextView) convertView.findViewById(R.id.kr_word_hanja);
        System.out.println(convertView);
        hanja.setText(word.hanja);

        TextView pronun = (TextView) convertView.findViewById(R.id.kr_word_pronun);
        pronun.setText(word.pronun);

        TextView wordclass = (TextView) convertView.findViewById(R.id.kr_word_class);
        String classes = Arrays.toString(word.wclass);
        if(classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) convertView.findViewById(R.id.kr_word_meaning);
        meaning.setText(word.def);

        return convertView;
    }

    @Override
    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        return new KrDetailsVisualizer((KrWordEntry) item);
    }
}
