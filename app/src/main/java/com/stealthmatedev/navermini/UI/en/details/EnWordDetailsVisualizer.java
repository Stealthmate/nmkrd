package com.stealthmatedev.navermini.UI.en.details;

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
import com.stealthmatedev.navermini.UI.SectionedListAdapter;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.en.EnWord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.stealthmatedev.navermini.App.APPTAG;

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


    private static class ExAdapter extends CustomizableArrayAdapter<String> {

        ExAdapter(Context context, EnWord.WordClassGroup.Meaning meaning) {
            super(context, R.layout.view_listitem_minimal);
            for(TranslatedExample ex : meaning.ex) this.add(ex.ex + " - " + ex.tr);
            this.setViewStyler(new ViewStyler() {
                @Override
                public void style(View v, int position) {
                    TextView tv = (TextView) v;
                    ViewGroup.LayoutParams lp = tv.getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    tv.setLayoutParams(lp);
                    tv.invalidate();
                    //tv.setTextIsSelectable(true);
                }
            });
        }
    }

    static void setDefinition(View root, EnWord.WordClassGroup.Meaning meaning) {

        if(meaning.ex.size() == 0) {
            root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.GONE);
            return;
        }

        Log.i(APPTAG, meaning.ex.size() + "");

        root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.VISIBLE);

        ListView exlist = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);
        exlist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        exlist.setAdapter(new ExAdapter(root.getContext(), meaning));
    }

    private EnWord details;

    @Override
    public void populate(String data) {
        this.details = new Gson().fromJson(data, EnWord.class);
    }

    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_en_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView extra = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);
        String extraStr = details.hanja;
        if(extraStr.length() == 0) extraStr = details.pronun;
        extra.setText(extraStr);

        ListView defs = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);;
        defs.setAdapter(WCGAdapter.makeAdapter(container, details));


        return view;
    }

    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
