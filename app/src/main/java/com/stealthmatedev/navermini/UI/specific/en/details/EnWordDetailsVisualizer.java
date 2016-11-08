package com.stealthmatedev.navermini.UI.specific.en.details;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.SectionedListAdapter;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.en.EnWord;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class EnWordDetailsVisualizer extends DetailsVisualizer {

    private static class WCGAdapter extends SectionedListAdapter<EnWord.WordClassGroup, EnWord.WordClassGroup.Meaning> {

        private static WCGAdapter makeAdapter(ViewGroup parent, EnWord details) {
            LinkedHashMap<EnWord.WordClassGroup, ArrayList<EnWord.WordClassGroup.Meaning>> map = new LinkedHashMap<>();

            for(EnWord.WordClassGroup grp : details.clsgrps) {
                map.put(grp, grp.meanings);
            }

            return new WCGAdapter(parent, map);
        }

        private WCGAdapter(final ViewGroup parent, LinkedHashMap<EnWord.WordClassGroup, ArrayList<EnWord.WordClassGroup.Meaning>> sectionMap) {
            super(sectionMap, new OnMeaningClickedListener<EnWord.WordClassGroup, EnWord.WordClassGroup.Meaning>() {
                @Override
                public void clicked(EnWord.WordClassGroup grp, EnWord.WordClassGroup.Meaning m) {
                    setDefinition(parent, m);
                }
            });
        }

        @Override
        protected String getHeaderText(EnWord.WordClassGroup header) {
            return header.wclass;
        }

        @Override
        protected String getMeaningText(EnWord.WordClassGroup.Meaning meaning) {
            return meaning.m;
        }
    }

    private static class ExAdapter extends ArrayAdapter<String> {

        ExAdapter(Context context, EnWord.WordClassGroup.Meaning meaning) {
            super(context, R.layout.view_listitem_text_wide);
            for(TranslatedExample ex : meaning.ex) this.add(ex.ex + " - " + ex.tr);
        }
    }

    private static void setDefinition(View root, EnWord.WordClassGroup.Meaning meaning) {

        if(meaning.ex.size() == 0) {
            root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.GONE);
            return;
        }
        root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.VISIBLE);

        ListView exlist = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);
        exlist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        exlist.getLayoutTransition().setDuration(1);
        exlist.setAdapter(new ExAdapter(root.getContext(), meaning));
    }

    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_generic_detail_word, container, false);

        EnWord details = (EnWord) getDetails();

        if(details == null) return view;

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView extra = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);
        String extraStr = details.hanja;
        if(extraStr.length() == 0) extraStr = details.pronun;
        extra.setText(extraStr);

        ListView defs = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);
        WCGAdapter adapter = WCGAdapter.makeAdapter(container, details);
        defs.setAdapter(adapter);
        
        defs.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        defs.getLayoutTransition().setDuration(100);
        defs.setOnItemClickListener(adapter.getSubitemClickListener());

        setDefinition(view, details.clsgrps.get(0).meanings.get(0));

        return view;
    }
}
