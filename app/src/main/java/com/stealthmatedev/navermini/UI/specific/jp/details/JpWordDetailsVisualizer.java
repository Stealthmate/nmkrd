package com.stealthmatedev.navermini.UI.specific.jp.details;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.SectionedListAdapter;
import com.stealthmatedev.navermini.data.CallbackAsyncTask;
import com.stealthmatedev.navermini.data.SentenceEntry;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.stealthmatedev.navermini.App.APPTAG;
import static com.stealthmatedev.navermini.data.SentenceEntry.Language.JP;
import static com.stealthmatedev.navermini.data.SentenceEntry.Language.KR;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private static final int CONTEXT_MENU_ID_COPY = 0;
    private static final int CONTEXT_MENU_ID_SAVE = 1;


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
        glossAdapter = makeGlossAdapter(meaning);
        glossList.setAdapter(glossAdapter);
    }

    private WCGAdapter meaningAdapter;
    private GlossAdapter glossAdapter;

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

        meaningAdapter = makeWCGAdapter(container, details);
        deflist.setAdapter(meaningAdapter);
        deflist.setOnItemClickListener(meaningAdapter.getSubitemClickListener());
        containerFragment.registerForContextMenu(deflist);


        ListView glossList = (ListView) view.findViewById(R.id.view_generic_detail_word_defex_list);
        containerFragment.registerForContextMenu(glossList);

        setDefinition(view, details.clsgrps.get(0).meanings.get(0));

        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        ListAdapter adapter = ((ListView) view).getAdapter();

        ListView lv = (ListView) view;
        menu.add(Menu.NONE, CONTEXT_MENU_ID_COPY, 0, android.R.string.copy).setActionView(view);
        if (lv.getId() == R.id.view_generic_detail_word_defex_list && adapter.getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position) instanceof TranslatedExample)
            menu.add(Menu.NONE, CONTEXT_MENU_ID_SAVE, 0, R.string.label_menu_save).setActionView(view);
    }

    @Override
    public boolean onContextItemSelected(final Fragment containerFragment, MenuItem menuItem) {
        String text = "WHAT THE FUCK";

        switch (menuItem.getItemId()) {
            case CONTEXT_MENU_ID_COPY: {
                int pos = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
                Object item = ((ListView) menuItem.getActionView()).getAdapter().getItem(pos);
                if (item instanceof Meaning.Gloss) text = ((Meaning.Gloss) item).g;
                else if (item instanceof Meaning) text = ((Meaning) item).m;
            }
            break;
            case CONTEXT_MENU_ID_SAVE: {
                int pos = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
                TranslatedExample item = (TranslatedExample) ((ListView) menuItem.getActionView()).getAdapter().getItem(pos);

                SentenceEntry.Language from, to;
                String word = ((JpWord) getDetails()).word;
                String kanji = ((JpWord) getDetails()).kanji;
                if(item.ex.contains(JpWord.stripFurigana(word))) {
                    from = KR;
                    to = JP;
                } else {
                    from = JP;
                    to = KR;
                }
                SentenceEntry sent = new SentenceEntry(from, to, kanji, item.ex, item.tr);
                StateManager.getState(containerFragment.getContext()).sentenceStore().put(sent, new CallbackAsyncTask.Callback() {
                    @Override
                    public void callback(Object param) {
                        Toast.makeText(containerFragment.getContext(), R.string.toast_saved, Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        }


        ClipboardManager cbm = (ClipboardManager) containerFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));

        Toast.makeText(containerFragment.getContext(), R.string.toast_copied, Toast.LENGTH_SHORT).show();

        return true;
    }
}
