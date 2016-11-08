package com.stealthmatedev.navermini.UI.specific.jp.details;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.generic.FixedListView;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpKanjiDetailsVisualizer extends DetailsVisualizer {

    private static final int CONTEXT_MENU_ID_KUNYOMI = 0;
    private static final int CONTEXT_MENU_ID_ONYOMI = 1;
    private static final int CONTEXT_MENU_ID_KRYOMI = 2;
    private static final int CONTEXT_MENU_ID_MEANING = 1;
    private static final int CONTEXT_MENU_ID_EX = 2;
    private static final int CONTEXT_MENU_ID_YOMI_EX = 3;

    private class KanjiMeaningsAdapter extends ArrayAdapter<JpKanji.Meaning> {

        private class ExAdapter extends CustomizableArrayAdapter<String> {

            ExAdapter(Context context, int resource, ArrayList<TranslatedExample> items) {
                super(context, resource);
                for (TranslatedExample ex : items) {
                    this.add(ex.ex + " - " + ex.tr);
                }
            }
        }

        private Fragment frag;

        KanjiMeaningsAdapter(Fragment fragment, ArrayList<JpKanji.Meaning> meanings) {
            super(fragment.getContext(), 0, meanings);
            this.frag = fragment;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            JpKanji.Meaning m = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_jp_kanji_listitem_meanings, parent, false);

            TextView meaning = (TextView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_meaning);
            meaning.setText(m.m);

            FixedListView ex = (FixedListView) convertView.findViewById(R.id.view_detail_jp_kanji_listitem_meaning_ex);
            frag.registerForContextMenu(ex);
            ex.setAdapter(new ExAdapter(getContext(), R.layout.view_listitem_furigana, m.ex));

            return convertView;
        }
    }

    private static class WordExAdapter extends CustomizableArrayAdapter<String> {
        WordExAdapter(Context context, int resource, ArrayList<JpKanji.WordLinkPair> wordexs) {
            super(context, resource);
            for (JpKanji.WordLinkPair wordex : wordexs) {
                this.add(wordex.word);
            }
        }
    }

    private static class LinkListener implements AdapterView.OnItemClickListener {

        private final ArrayList<JpKanji.WordLinkPair> links;
        private final Context context;

        private LinkListener(Context fragment, ArrayList<JpKanji.WordLinkPair> links) {
            this.links = links;
            this.context = fragment;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final StateManager state = StateManager.getState(parent.getContext());
            state.openDetails(new JpWord(links.get(position).lnk), true);
        }
    }

    @Override
    public View getView(Fragment containerFragment, final ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_jp_detail_kanji, container, false);

        JpKanji details = (JpKanji) getDetails();

        if (details == null) return view;

        TextView kanji = (TextView) view.findViewById(R.id.view_detail_jp_kanji_kanji);
        kanji.setText(String.valueOf(details.kanji));

        TextView strokes = (TextView) view.findViewById(R.id.view_jp_details_kanji_strokes);
        strokes.setText(String.valueOf(details.strokes));

        TextView radical = (TextView) view.findViewById(R.id.view_detail_jp_kanji_radical);
        radical.setText(String.valueOf(details.radical));

        ListView krlist = (ListView) view.findViewById(R.id.view_detail_jp_kanji_kr_readings);
        if (details.kr.size() == 0) {
            krlist.setVisibility(View.GONE);
            view.findViewById(R.id.view_detail_jp_kanji_kr_readings).setVisibility(View.GONE);
        } else {
            krlist.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.kr));
            containerFragment.registerForContextMenu(krlist);
        }

        ListView kunlist = (ListView) view.findViewById(R.id.view_detail_jp_kanji_kunyomi);
        if (details.kunyomi.size() == 0) {
            kunlist.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_entry_kanji_kunyomi_label).setVisibility(View.GONE);
        } else {
            kunlist.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.kunyomi));
            containerFragment.registerForContextMenu(kunlist);
        }

        ListView onlist = (ListView) view.findViewById(R.id.view_detail_jp_kanji_onyomi);
        if (details.onyomi.size() == 0) {
            kunlist.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_entry_kanji_onyomi_label).setVisibility(View.GONE);
        } else {
            onlist.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.onyomi));
            containerFragment.registerForContextMenu(onlist);
        }


        FixedListView meanings = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_meanings);
        if (details.meanings.size() == 0) {
            meanings.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_detail_kanji_label_meanings).setVisibility(View.GONE);
        } else {
            meanings.setAdapter(new KanjiMeaningsAdapter(containerFragment, details.meanings));
        }

        CustomizableArrayAdapter.ViewStyler linkStyler = new CustomizableArrayAdapter.ViewStyler() {
            @Override
            public void style(View v, int position) {
                ((TextView) v).setTextColor(ContextCompat.getColor(container.getContext(), R.color.nm_colorTextLink));
            }
        };

        FixedListView kunex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_kunex);
        if (details.kunex.size() == 0) {
            kunex.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_detail_kanji_label_kunex).setVisibility(View.GONE);
        } else {

            kunex.setAdapter(new WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.kunex));
            ((CustomizableArrayAdapter) kunex.getAdapter()).setViewStyler(linkStyler);
            kunex.setOnItemClickListener(new LinkListener(container.getContext(), details.kunex));
            containerFragment.registerForContextMenu(kunex);
        }

        FixedListView onex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_onex);
        if (details.onex.size() == 0) {

            onex.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_detail_kanji_label_onex).setVisibility(View.GONE);
        } else {
            onex.setAdapter(new WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.onex));
            ((CustomizableArrayAdapter) onex.getAdapter()).setViewStyler(linkStyler);
            onex.setOnItemClickListener(new LinkListener(container.getContext(), details.onex));
            containerFragment.registerForContextMenu(onex);
        }

        return view;

    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        ListView lv = ((ListView) view);
        int menuid = lv.getId();
        menu.add(Menu.NONE, menuid, 0, R.string.label_menu_copy).setActionView(lv);
    }

    @Override
    public boolean onContextItemSelected(Fragment containerFragment, MenuItem menuItem) {

        String text = "WHAT THE FUCK";

        int position = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;

        ListView lv = (ListView) menuItem.getActionView();
        Object item = lv.getAdapter().getItem(position);

        switch (menuItem.getItemId()) {
            case R.id.view_detail_jp_kanji_kunyomi:
            case R.id.view_detail_jp_kanji_onyomi:
            case R.id.view_detail_jp_kanji_kr_readings:
            case R.id.view_detail_jp_kanji_list_kunex:
            case R.id.view_detail_jp_kanji_list_onex:
            case R.id.view_detail_jp_kanji_listitem_meaning_ex: {
                text = (String) item;
            }
            break;
            case R.id.view_detail_jp_kanji_list_meanings: {
                text = ((JpKanji.Meaning) item).m;
            }
            break;
        }


        ClipboardManager cbm = (ClipboardManager) containerFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));

        Log.d(APPTAG, "Copied " + text);
        Toast.makeText(containerFragment.getContext(), "Copied", Toast.LENGTH_SHORT).show();

        return true;
    }
}
