package com.stealthmateoriginal.navermini.UI.jp;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
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

    public JpWordDetailsAdapter(StateManager state, JpWordEntry word, String response) {
        super(state, response);
        WordDetails details = null;
        try {
            details = new WordDetails(word, response);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }

        System.out.println("Details initialized. ");
        System.out.println(response);
        if(response.equals("[]")) details = new WordDetails(word);

        this.details = details;
    }

    public void setDefinition(Meaning meaning) {

        ViewGroup root = (ViewGroup) state.getDetailsFragment().getView();
        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.getMeaning());

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new GlossAdapter(state.getActivity(), meaning.getGlosses()));
    }

    @Override
    public void populateContainer(View container) {
        ViewGroup.inflate(state.getActivity(), R.layout.layout_jp_detail_word, (ViewGroup) container);

        TextView name = (TextView) container.findViewById(R.id.view_jp_detail_word_name);
        name.setText(details.word.getName());

        TextView kanji = (TextView) container.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(details.word.getKanji());

        final ListView deflist = (ListView) container.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WordclassAdapter adapter = new WordclassAdapter(state.getActivity(), this, details);
        deflist.setAdapter(adapter);

        System.out.println(Arrays.toString(details.getWordclasses().toArray()));
        setDefinition(details.getMeaningsForWordclass(adapter.getItem(0)).get(0));
    }
}
