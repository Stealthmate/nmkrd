package com.stealthmatedev.navermini.UI.specific.jp.details;

import android.animation.LayoutTransition;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.SectionedListAdapter;
import com.stealthmatedev.navermini.UI.specific.en.details.EnWordDetailsVisualizer;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private static final int CONTEXT_MENU_ID_DEFS = 0;
    private static final int CONTEXT_MENU_ID_EX = 1;


    private WCGAdapter makeWCGAdapter(ViewGroup parent, JpWord details) {
        LinkedHashMap<JpWord.WordClassGroup, ArrayList<JpWord.WordClassGroup.Meaning>> map = new LinkedHashMap<>();

        for (JpWord.WordClassGroup grp : details.clsgrps) {
            map.put(grp, grp.meanings);
        }

        return new WCGAdapter(parent, map);

    }

    private GlossAdapter makeGlossAdapter(JpWord.WordClassGroup.Meaning meaning) {
        LinkedHashMap<JpWord.WordClassGroup.Meaning.Gloss, ArrayList<TranslatedExample>> map = new LinkedHashMap<>();

        for (JpWord.WordClassGroup.Meaning.Gloss gloss : meaning.glosses) {
            map.put(gloss, gloss.ex);
        }

        return new GlossAdapter(map);
    }

    private class GlossAdapter extends SectionedListAdapter<JpWord.WordClassGroup.Meaning.Gloss, TranslatedExample> {


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

    private class WCGAdapter extends SectionedListAdapter<JpWord.WordClassGroup, Meaning> {


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
            if (meaning.m.length() > 0) return meaning.m;
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

    private void setDefinition(View root, Meaning meaning) {
        ListView glossList = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);

        if (meaning.glosses.size() == 1 && meaning.glosses.get(0).ex.size() == 0) {
            root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.GONE);
            return;
        }
        root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.VISIBLE);

        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.getLayoutTransition().setDuration(100);
        glossList.setAdapter(makeGlossAdapter(meaning));
    }

    @Override
    public View getView(Fragment containerFragment, ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_generic_detail_word, container, false);

        JpWord details = (JpWord) getDetails();

        if (details.word.length() == 0) return view;

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WCGAdapter adapter = makeWCGAdapter(container, details);
        deflist.setAdapter(adapter);
        deflist.setOnItemClickListener(adapter.getSubitemClickListener());
        containerFragment.registerForContextMenu(deflist);


        ListView glossList = (ListView) view.findViewById(R.id.view_generic_detail_word_defex_list);
        containerFragment.registerForContextMenu(glossList);

        setDefinition(view, details.clsgrps.get(0).meanings.get(0));

        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        ListAdapter adapter = ((ListView) view).getAdapter();
        if (adapter instanceof JpWordDetailsVisualizer.GlossAdapter) {
            menu.add(Menu.NONE, CONTEXT_MENU_ID_EX, 0, "Copy");
        } else if (adapter.getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position) instanceof JpWord.WordClassGroup.Meaning) {
            menu.add(Menu.NONE, CONTEXT_MENU_ID_DEFS, 0, "Copy");
        }
    }

    @Override
    public boolean onContextItemSelected(Fragment containerFragment, MenuItem item) {
        return false;
    }
}
