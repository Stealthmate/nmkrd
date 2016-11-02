package com.stealthmatedev.navermini.UI.jp.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.generic.FixedListView;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.TranslatedExample;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.state.DetailsDictionary;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpKanjiDetailsVisualizer extends DetailsVisualizer {

    private static class KanjiMeaningsAdapter extends ArrayAdapter<JpKanji.Meaning> {

        private static class ExAdapter extends CustomizableArrayAdapter<String> {

            ExAdapter(Context context, int resource, ArrayList<TranslatedExample> items) {
                super(context, resource);
                for (TranslatedExample ex : items) {
                    this.add(ex.ex + " - " + ex.tr);
                }
            }
        }

        KanjiMeaningsAdapter(Context context, ArrayList<JpKanji.Meaning> meanings) {
            super(context, 0, meanings);
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
            try {
                String url = DetailsDictionary.JAPANESE_DETAILS.path + "?lnk=" + URLEncoder.encode(links.get(position).lnk, "utf-8");

                final StateManager state = StateManager.getState(parent.getContext());
                final DetailsFragment dfrag = state.openDetailsPage();
                final JpWordDetailsVisualizer visualizer = new JpWordDetailsVisualizer();

                state.getSearchEngine().queryDetails(url, new SearchEngine.OnResponse() {
                    @Override
                    public void responseReady(String response) {
                        visualizer.populate(new DetailedItem.Translator(JpWord.class).translate(response));
                    }

                    @Override
                    public void onError(VolleyError err) {
                        state.closePage(dfrag);
                    }
                });


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View getView(final ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_jp_detail_kanji, container, false);

        JpKanji details = (JpKanji) getDetails();

        if (details == null) return view;

        TextView kanji = (TextView) view.findViewById(R.id.view_detail_jp_kanji_kanji);
        kanji.setText(String.valueOf(details.kanji));

        TextView strokes = (TextView) view.findViewById(R.id.view_jp_details_kanji_strokes);
        strokes.setText(String.valueOf(details.strokes));

        TextView radical = (TextView) view.findViewById(R.id.view_detail_jp_kanji_radical);
        radical.setText(String.valueOf(details.radical));

        ListLayout krlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_kr_readings);
        krlist.clear();
        krlist.populate(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.kr));

        ListLayout kunlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_kunyomi);
        kunlist.clear();
        kunlist.populate(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.kunyomi));

        ListLayout onlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_onyomi);
        onlist.clear();
        onlist.populate(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_minimal, details.onyomi));

        FixedListView meanings = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_meanings);
        meanings.setAdapter(new KanjiMeaningsAdapter(container.getContext(), details.meanings));

        CustomizableArrayAdapter.ViewStyler linkStyler = new CustomizableArrayAdapter.ViewStyler() {
            @Override
            public void style(View v, int position) {
                ((TextView) v).setTextColor(ContextCompat.getColor(container.getContext(), R.color.nm_colorTextLink));
            }
        };

        FixedListView kunex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_kunex);
        kunex.setAdapter(new WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.kunex));
        ((CustomizableArrayAdapter) kunex.getAdapter()).setViewStyler(linkStyler);
        kunex.setOnItemClickListener(new LinkListener(container.getContext(), details.kunex));

        FixedListView onex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_onex);
        onex.setAdapter(new WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.onex));
        ((CustomizableArrayAdapter) onex.getAdapter()).setViewStyler(linkStyler);
        onex.setOnItemClickListener(new LinkListener(container.getContext(), details.onex));

        return view;

    }
}
