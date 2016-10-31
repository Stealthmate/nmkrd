package com.stealthmatedev.navermini.UI.jp.details.kanji;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.CustomizableArrayAdapter;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.generic.FixedListView;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.UI.jp.details.word.JpWordDetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.kanjidetails.KanjiDetails;
import com.stealthmatedev.navermini.state.DetailsDictionary;
import com.stealthmatedev.navermini.state.StateManager;

import org.json.JSONException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpKanjiDetailsVisualizer extends DetailsVisualizer {

    private static class WordExAdapter extends CustomizableArrayAdapter<String> {
        public WordExAdapter(Context context, int resource, ArrayList<JpKanji.WordLinkPair> wordexs) {
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
                StateManager.getState(context).loadDetailsAsync(url, new JpWordDetailsVisualizer(), null);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private JpKanji details;

    public JpKanjiDetailsVisualizer() {
        super();
    }

    @Override
    public void populate(String data) {
        this.details = new Gson().fromJson(data, JpKanji.class);
    }

    @Override
    public View getView(final ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_jp_detail_kanji, container, false);

        if(details == null) return view;

        TextView kanji = (TextView) view.findViewById(R.id.view_detail_jp_kanji_kanji);
        kanji.setText("" + details.kanji);

        TextView strokes = (TextView) view.findViewById(R.id.view_jp_details_kanji_strokes);
        strokes.setText("" + details.strokes);

        TextView radical = (TextView) view.findViewById(R.id.view_detail_jp_kanji_radical);
        radical.setText("" + details.radical);

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
            public void style(View v) {
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

    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
