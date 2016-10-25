package com.stealthmateoriginal.navermini.UI.jp;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;
import com.stealthmateoriginal.navermini.UI.jp.worddetails.GlossAdapter;
import com.stealthmateoriginal.navermini.UI.jp.worddetails.WordclassAdapter;
import com.stealthmateoriginal.navermini.data.jp.JpWordEntry;
import com.stealthmateoriginal.navermini.data.jp.worddetails.Meaning;
import com.stealthmateoriginal.navermini.data.jp.worddetails.WordDetails;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsAdapter extends DetailsAdapter {

    private final WordDetails details;

    public JpWordDetailsAdapter(DetailsFragment fragment, String response) {
        super(fragment);
        WordDetails details = null;
        try {
            details = new WordDetails(response);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }

        this.details = details;
    }

    public JpWordDetailsAdapter(DetailsFragment fragment, JpWordEntry word) {
        super(fragment);
        this.details = new WordDetails(word);
    }

    public void setDefinition(Meaning meaning) {

        ViewGroup root = (ViewGroup) fragment.getView();
        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(fragment.getActivity(), meaning.getGlosses()));
    }

    @Override
    public View getView(ViewGroup container) {

        System.out.println("EXCEPTION");
        System.out.println(fragment);
        System.out.println(fragment.getActivity());
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.layout_jp_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_jp_detail_word_name);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WordclassAdapter adapter = new WordclassAdapter(fragment.getActivity(), this, details);
        deflist.setAdapter(adapter);

        Meaning meaning = details.getMeaningsForWordclass(adapter.getItem(0)).get(0);

        TextView meaningView = (TextView) view.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) view.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(fragment.getActivity(), meaning.getGlosses()));

        return view;
    }


    @Override
    public void save(Bundle outState) {
        outState.putString("nm_DETAILS_CLASS", this.getClass().getCanonicalName());
        outState.putSerializable("nm_DETAILS_DATA", details);
    }
}
