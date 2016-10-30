package com.stealthmatedev.navermini.UI.en.details;

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
import com.stealthmatedev.navermini.data.en.EnWordDetails;
import com.stealthmatedev.navermini.data.en.worddetails.Definition;

import org.json.JSONException;

import java.io.Serializable;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class EnWordDetailsVisualizer extends DetailsVisualizer {

    public static void setDefinition(Context context, View root, Definition meaning) {
/*
        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(def.getMeaning());

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(context, def.getGlosses()));*/
    }

    private EnWordDetails details;

    @Override
    public void populate(String data) {
        this.details = new Gson().fromJson(data, EnWordDetails.class);
    }

    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_en_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_en_detail_word_name);
        name.setText(details.word);

        TextView extra = (TextView) view.findViewById(R.id.view_en_detail_word_extra);
        extra.setText(details.extra);

        ListView defs = (ListView) view.findViewById(R.id.view_en_detail_word_deflist);
        defs.setAdapter(new WordClassAdapter(container.getContext(), details.clsgrps));

        return view;
    }

    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
