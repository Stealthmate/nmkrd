package com.stealthmatedev.navermini.UI.specific.jp.details;

import android.animation.LayoutTransition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.SectionedListAdapter;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private static class GlossAdapter extends SectionedListAdapter<JpWord.WordClassGroup.Meaning.Gloss, TranslatedExample> {

        private static GlossAdapter makeAdapter(JpWord.WordClassGroup.Meaning meaning) {
            LinkedHashMap<JpWord.WordClassGroup.Meaning.Gloss, ArrayList<TranslatedExample>> map = new LinkedHashMap<>();

            for(JpWord.WordClassGroup.Meaning.Gloss gloss : meaning.glosses) {
                map.put(gloss, gloss.ex);
            }

            return new GlossAdapter(map);
        }

        private GlossAdapter(LinkedHashMap<Meaning.Gloss, ArrayList<TranslatedExample>> sectionMap) {
            super(sectionMap);
        }

        @Override
        protected String getHeaderText(Meaning.Gloss header) {
            return header.g;
        }

        @Override
        protected String getMeaningText(TranslatedExample meaning) {
            return meaning.ex + " - " + meaning.tr;
        }
    }

    private static class WCGAdapter extends SectionedListAdapter<JpWord.WordClassGroup, Meaning> {

        private static WCGAdapter makeAdapter(ViewGroup parent, JpWord details) {
            LinkedHashMap<JpWord.WordClassGroup, ArrayList<JpWord.WordClassGroup.Meaning>> map = new LinkedHashMap<>();

            for(JpWord.WordClassGroup grp : details.clsgrps) {
                map.put(grp, grp.meanings);
            }

            return new WCGAdapter(parent, map);

        }

        private WCGAdapter(final ViewGroup parent, LinkedHashMap<JpWord.WordClassGroup, ArrayList<JpWord.WordClassGroup.Meaning>> map) {
            super(map, new OnMeaningClickedListener<JpWord.WordClassGroup, Meaning>() {
                @Override
                public void clicked(JpWord.WordClassGroup grp, Meaning m) {
                    setDefinition(parent, m);
                }
            });
        }

        @Override
        protected String getHeaderText(JpWord.WordClassGroup header) {
            return header.wclass;
        }

        @Override
        protected String getMeaningText(Meaning meaning) {
            if(meaning.m.length() > 0) return meaning.m;
            return meaning.glosses.get(0).g;
        }
    }


    public JpWordDetailsVisualizer() {
        super();
    }

    public JpWordDetailsVisualizer(JpWord word) {
        super();
        this.populate(word);
    }

    public JpWordDetailsVisualizer(Serializable data) {
        super();
        throw new UnsupportedOperationException("JpWordDetailsVisualizer: constructor from serialized data not yet supported.");
        //this.details = new KrWord(data);
    }

    private static void setDefinition(View root, Meaning meaning) {
        ListView glossList = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);

        if(meaning.glosses.size() == 1 && meaning.glosses.get(0).ex.size() == 0) {
            root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.GONE);
            return;
        }
        root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.VISIBLE);

        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.getLayoutTransition().setDuration(100);
        glossList.setAdapter(GlossAdapter.makeAdapter(meaning));
    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_generic_detail_word, container, false);

        JpWord details = (JpWord) getDetails();

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WCGAdapter adapter = WCGAdapter.makeAdapter(container, details);
        deflist.setAdapter(adapter);
        deflist.setOnItemClickListener(adapter.getSubitemClickListener());

        setDefinition(view, details.clsgrps.get(0).meanings.get(0));

        return view;
    }
}
