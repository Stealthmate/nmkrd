package com.stealthmatedev.navermini.UI.specific.en.details;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.stealthmatedev.navermini.data.SentenceEntry.Language.EN;
import static com.stealthmatedev.navermini.data.SentenceEntry.Language.JP;
import static com.stealthmatedev.navermini.data.SentenceEntry.Language.KR;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class EnWordDetailsVisualizer extends DetailsVisualizer {

    private static final int CONTEXT_MENU_ID_COPY = 0;
    private static final int CONTEXT_MENU_ID_SAVE = 1;

    private WCGAdapter makeAdapter(ViewGroup parent, EnWord details) {
        LinkedHashMap<EnWord.WordClassGroup, ArrayList<EnWord.WordClassGroup.Meaning>> map = new LinkedHashMap<>();

        for (EnWord.WordClassGroup grp : details.clsgrps) {
            map.put(grp, grp.meanings);
        }

        return new WCGAdapter(parent, map);
    }

    private class WCGAdapter extends SectionedListAdapter<EnWord.WordClassGroup, EnWord.WordClassGroup.Meaning> {

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

    private class ExAdapter extends ArrayAdapter<String> {

        ExAdapter(Context context, EnWord.WordClassGroup.Meaning meaning) {
            super(context, R.layout.view_listitem_text_wide);
            for (TranslatedExample ex : meaning.ex) this.add(ex.ex + " - " + ex.tr);
        }
    }

    private void setDefinition(View root, EnWord.WordClassGroup.Meaning meaning) {

        if (meaning.ex.size() == 0) {
            root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.GONE);
            return;
        }
        root.findViewById(R.id.view_generic_detail_word_defex_container).setVisibility(View.VISIBLE);

        ListView exlist = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);
        exlist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        exlist.getLayoutTransition().setDuration(1);
        this.exadapter = new ExAdapter(root.getContext(), meaning);
        exlist.setAdapter(this.exadapter);
    }

    private WCGAdapter wcgadapter;
    private ExAdapter exadapter;

    @Override
    public View getView(Fragment containerFragment, ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_generic_detail_word, container, false);

        EnWord details = (EnWord) getDetails();

        if (details == null) return view;

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView extra = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);
        String extraStr = details.hanja;
        if (extraStr.length() == 0) extraStr = details.pronun;
        extra.setText(extraStr);

        ListView defs = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);

        containerFragment.registerForContextMenu(defs);

        this.wcgadapter = makeAdapter(container, details);
        defs.setAdapter(wcgadapter);

        defs.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        defs.getLayoutTransition().setDuration(100);
        defs.setOnItemClickListener(wcgadapter.getSubitemClickListener());


        ListView exlist = (ListView) view.findViewById(R.id.view_generic_detail_word_defex_list);
        containerFragment.registerForContextMenu(exlist);

        setDefinition(view, details.clsgrps.get(0).meanings.get(0));

        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(Menu.NONE, CONTEXT_MENU_ID_COPY, 0, android.R.string.copy).setActionView(view);
        if (view.getId() == R.id.view_generic_detail_word_defex_list) {
            menu.add(Menu.NONE, CONTEXT_MENU_ID_SAVE, 0, R.string.label_menu_save).setActionView(view);
        }
    }

    @Override
    public boolean onContextItemSelected(final Fragment containerFragment, MenuItem menuItem) {

        String text = "WHAT THE FUCK";

        switch (menuItem.getItemId()) {
            case CONTEXT_MENU_ID_COPY: {
                int pos = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
                Object item = ((ListView) menuItem.getActionView()).getAdapter().getItem(pos);
                if (item instanceof EnWord.WordClassGroup.Meaning)
                    text = ((EnWord.WordClassGroup.Meaning) item).m;
                else if (item instanceof String) text = (String) item;
            }
            break;
            case CONTEXT_MENU_ID_SAVE: {
                int pos = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
                String item = (String) ((ListView) menuItem.getActionView()).getAdapter().getItem(pos);

                SentenceEntry.Language from, to;
                String word = ((EnWord) getDetails()).word;
                String ex = item.split(" - ")[0];
                String tr = item.split(" - ")[1];
                if (ex.contains(word)) {
                    from = KR;
                    to = EN;
                } else {
                    from = EN;
                    to = KR;
                }
                SentenceEntry sent = new SentenceEntry(from, to, word, ex, tr);
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
