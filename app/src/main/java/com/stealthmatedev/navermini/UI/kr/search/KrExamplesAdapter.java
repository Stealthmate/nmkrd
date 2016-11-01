package com.stealthmatedev.navermini.UI.kr.search;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.ResultListAdapter;
import com.stealthmatedev.navermini.data.kr.KrExampleEntry;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExamplesAdapter extends ResultListAdapter {

    public KrExamplesAdapter(StateManager state, ResultListQuery query, String response) {
        super(state, query, response);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {
        ArrayList<DetailedItem> examples = null;
        Gson gson = new Gson();
        //Kr examples are single-string without translation
        String[] entries = gson.fromJson(result, String[].class);

        examples = new ArrayList<>(entries.length);
        for (int i = 0; i <= entries.length - 1; i++) {
            examples.add(new KrExampleEntry(entries[i]));
        }
        return examples;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof TextView)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_text_large, parent, false);
        }

        TextView tv = (TextView) convertView;

        KrExampleEntry ex = (KrExampleEntry) getItem(position);

        if(ex == null) return tv;

        tv.setText(ex.getText());

        return tv;
    }

    @Override
    protected DetailsVisualizer getDetailsVisualizer(DetailedItem item) {
        return null;
    }
}
