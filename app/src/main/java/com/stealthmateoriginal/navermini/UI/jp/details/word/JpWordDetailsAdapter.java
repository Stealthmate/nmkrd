package com.stealthmateoriginal.navermini.UI.jp.details.word;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.jp.details.word.GlossAdapter;
import com.stealthmateoriginal.navermini.UI.jp.details.word.WordclassAdapter;
import com.stealthmateoriginal.navermini.data.jp.JpWordEntry;
import com.stealthmateoriginal.navermini.data.jp.worddetails.Meaning;
import com.stealthmateoriginal.navermini.data.jp.worddetails.WordDetails;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsAdapter extends DetailsAdapter {

    private final WordDetails details;

    public JpWordDetailsAdapter(Context context, String response) {
        super(context);
        WordDetails details = null;
        try {
            details = new WordDetails(response);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }

        this.details = details;
    }

    public JpWordDetailsAdapter(Context context, JpWordEntry word) {
        super(context);
        this.details = new WordDetails(word);
    }

    public static void setDefinition(Context context, View root, Meaning meaning) {

        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(context, meaning.getGlosses()));
    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_jp_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_jp_detail_word_name);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WordclassAdapter adapter = new WordclassAdapter(context, this, details);
        deflist.setAdapter(adapter);

        Meaning meaning = details.getMeaningsForWordclass(adapter.getItem(0)).get(0);

        TextView meaningView = (TextView) view.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) view.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(context, meaning.getGlosses()));

        return view;
    }


    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
