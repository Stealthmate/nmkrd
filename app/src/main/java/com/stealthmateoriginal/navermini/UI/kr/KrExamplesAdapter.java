package com.stealthmateoriginal.navermini.UI.kr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.ResultListAdapter;
import com.stealthmateoriginal.navermini.data.kr.KrExampleEntry;
import com.stealthmateoriginal.navermini.state.DetailedItem;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/07 0007.
 */

public class KrExamplesAdapter extends ResultListAdapter {

    public KrExamplesAdapter(StateManager state, String query, String response) {
        super(state, query, response);
    }

    @Override
    protected ArrayList<DetailedItem> parseResult(String result) {

        ArrayList<DetailedItem> examples = null;

        try {
            JSONArray exarr = new JSONArray(result);
            examples = new ArrayList<>(exarr.length());
            for (int i = 0; i <= exarr.length() - 1; i++) {
                examples.add(new KrExampleEntry(exarr.getString(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("COULD NOT PARSE EXAMPLES");
        }

        return examples;
    }

    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        if(convertView == null || !(convertView instanceof TextView)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_listitem_kr_example, parent, false);
        }

        ((TextView) convertView).setText(((KrExampleEntry)getItem(position)).getText());

        return convertView;
    }
}
