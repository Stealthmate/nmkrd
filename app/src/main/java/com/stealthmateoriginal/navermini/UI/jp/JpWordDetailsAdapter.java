package com.stealthmateoriginal.navermini.UI.jp;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.jp.worddetails.Definition;
import com.stealthmateoriginal.navermini.UI.jp.worddetails.DefinitionAdapter;
import com.stealthmateoriginal.navermini.UI.kr.KrDetailsAdapter;
import com.stealthmateoriginal.navermini.state.StateManager;
import com.stealthmateoriginal.navermini.state.jp.JpWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsAdapter extends DetailsAdapter {

    private final JpWordEntry word;
    private ArrayList<Definition> definitions;

    public JpWordDetailsAdapter(StateManager state, JpWordEntry word, String response) {
        super(state, response);
        this.word = word;
        try {
            this.definitions = new ArrayList<>(parseResponse(response));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
            this.definitions = null;
        }
    }

    private List<Definition> parseResponse(String response) throws
            JSONException {
        JSONArray defarr = new JSONArray(response);

        ArrayList<Definition> defs = new ArrayList<>(defarr.length());

        for (int i = 0; i <= defarr.length() - 1; i++) {
            defs.add(new Definition(defarr.getJSONObject(i)));
        }

        return defs;
    }

    private void setDefinition(Definition def) {

    }

    @Override
    public void populateContainer(View container) {
        ViewGroup.inflate(state.getActivity(), R.layout.layout_jp_detail_word, (ViewGroup) container);

        TextView name = (TextView) container.findViewById(R.id.view_jp_detail_word_name);
        name.setText(word.getName());

        TextView kanji = (TextView) container.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(word.getKanji());

        final ListView deflist = (ListView) container.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        DefinitionAdapter adapter = new DefinitionAdapter(state.getActivity(), definitions);
        deflist.setAdapter(adapter);

        setDefinition(adapter.getItem(0));
    }
}
