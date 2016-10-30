package com.stealthmatedev.navermini.UI.jp.details.word;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpWordEntry;
import com.stealthmatedev.navermini.data.jp.worddetails.Meaning;
import com.stealthmatedev.navermini.data.jp.worddetails.WordDetails;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private WordDetails details;

    public JpWordDetailsVisualizer() {
        super();
    }

    public JpWordDetailsVisualizer(JpWordEntry word) {
        super();
        this.details = new WordDetails(word);
    }

    public JpWordDetailsVisualizer(Serializable data) {
        super();
        throw new UnsupportedOperationException("JpWordDetailsVisualizer: constructor from serialized data not yet supported.");
        //this.details = new KrWord(data);
    }

    public static void setDefinition(Context context, View root, Meaning meaning) {

        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(context, meaning.getGlosses()));
    }

    @Override
    public void populate(String data) {

        WordDetails details = null;
        try {
            details = new WordDetails(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.details = details;
    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_jp_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_jp_detail_word_name);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WordclassAdapter adapter = new WordclassAdapter(container.getContext(), details);
        deflist.setAdapter(adapter);

        Meaning meaning = details.getMeaningsForWordclass(adapter.getItem(0)).get(0);

        TextView meaningView = (TextView) view.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) view.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(container.getContext(), meaning.getGlosses()));

        return view;
    }


    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
