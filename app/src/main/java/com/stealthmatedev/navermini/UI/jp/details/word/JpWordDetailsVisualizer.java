package com.stealthmatedev.navermini.UI.jp.details.word;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;

import java.io.Serializable;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private JpWord details;

    public JpWordDetailsVisualizer() {
        super();
    }

    public JpWordDetailsVisualizer(JpWord word) {
        super();
        this.details = word;
    }

    public JpWordDetailsVisualizer(Serializable data) {
        super();
        throw new UnsupportedOperationException("JpWordDetailsVisualizer: constructor from serialized data not yet supported.");
        //this.details = new KrWord(data);
    }

    public static void setDefinition(Context context, View root, Meaning meaning) {

        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.m);

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(context, meaning.glosses));
    }

    @Override
    public void populate(String data) {
        this.details = new Gson().fromJson(data, JpWord.class);
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

        JpWord.WordClassGroup.Meaning meaning = details.clsgrps.get(0).meanings.get(0);

        TextView meaningView = (TextView) view.findViewById(R.id.view_jp_detail_word_definition);
        String meanstr = meaning.m;
        if(meanstr.length() == 0) meanstr = meaning.glosses.get(0).g;
        meaningView.setText(meaning.m);

        ListView glossList = (ListView) view.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(container.getContext(), meaning.glosses));

        return view;
    }


    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
