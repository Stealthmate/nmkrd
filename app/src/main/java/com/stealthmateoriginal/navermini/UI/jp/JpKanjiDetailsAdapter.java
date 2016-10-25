package com.stealthmateoriginal.navermini.UI.jp;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.CustomizableArrayAdapter;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.FixedListView;
import com.stealthmateoriginal.navermini.UI.ListLayout;
import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;
import com.stealthmateoriginal.navermini.UI.jp.kanjidetails.KanjiMeaningsAdapter;
import com.stealthmateoriginal.navermini.UI.jp.worddetails.WordclassAdapter;
import com.stealthmateoriginal.navermini.data.jp.kanjidetails.KanjiDetails;
import com.stealthmateoriginal.navermini.state.DetailedItem;
import com.stealthmateoriginal.navermini.state.DetailsDictionary;
import com.stealthmateoriginal.navermini.state.StateManager;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.view.View.inflate;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpKanjiDetailsAdapter extends DetailsAdapter {

    private static class WordExAdapter extends CustomizableArrayAdapter<String> {
        public WordExAdapter(Context context, int resource, ArrayList<Pair<String, String>> wordexs) {
            super(context, resource);
            for (Pair<String, String> wordex : wordexs) {
                this.add(wordex.first);
            }
        }
    }

    private static class LinkListener implements AdapterView.OnItemClickListener {

        private final ArrayList<Pair<String, String>> links;
        private final DetailsFragment fragment;

        private LinkListener(DetailsFragment fragment, ArrayList<Pair<String, String>> links) {
            this.links = links;
            this.fragment = fragment;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String link = links.get(position).second;
            fragment.getState().loadDetails(new DetailedItem() {
                @Override
                public boolean hasDetails() {
                    return true;
                }

                @Override
                public String getLinkToDetails() {
                    try {
                        return DetailsDictionary.JAPANESE_WORDS_DETAILS.path + "?lnk=" + URLEncoder.encode(link, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        System.err.println("WTF ENCODING ERROR");
                    }

                    return "";
                }

                @Override
                public DetailsAdapter createAdapterFromDetails(DetailsFragment fragment, String details) {
                    return new JpWordDetailsAdapter(fragment, details);
                }
            });

        }
    }

    KanjiDetails details;

    public JpKanjiDetailsAdapter(DetailsFragment fragment, String response) {
        super(fragment);
        try {
            details = new KanjiDetails(response);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("FAILED PARSING KANJI DETAILS");
        }
    }

    @Override
    public View getView(ViewGroup container) {

        System.out.println(fragment);
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.layout_jp_detail_kanji, container, false);

        TextView kanji = (TextView) view.findViewById(R.id.view_detail_jp_kanji_kanji);
        kanji.setText("" + details.kanji);

        TextView strokes = (TextView) view.findViewById(R.id.view_jp_details_kanji_strokes);
        strokes.setText("" + details.strokes);

        TextView radical = (TextView) view.findViewById(R.id.view_detail_jp_kanji_radical);
        radical.setText("" + details.radical);

        ListLayout krlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_kr_readings);
        krlist.clear();
        krlist.populate(new ArrayAdapter<>(fragment.getActivity(), R.layout.view_listitem_minimal, details.kr));

        ListLayout kunlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_kunyomi);
        kunlist.clear();
        kunlist.populate(new ArrayAdapter<>(fragment.getActivity(), R.layout.view_listitem_minimal, details.kunyomi));

        ListLayout onlist = (ListLayout) view.findViewById(R.id.view_detail_jp_kanji_onyomi);
        onlist.clear();
        onlist.populate(new ArrayAdapter<>(fragment.getActivity(), R.layout.view_listitem_minimal, details.onyomi));

        FixedListView meanings = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_meanings);
        meanings.setAdapter(new KanjiMeaningsAdapter(fragment.getActivity(), details.meanings));

        CustomizableArrayAdapter.ViewStyler linkStyler = new CustomizableArrayAdapter.ViewStyler() {
            @Override
            public void style(View v) {
                ((TextView) v).setTextColor(ContextCompat.getColor(fragment.getActivity(), R.color.nm_colorTextLink));
            }
        };

        FixedListView kunex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_kunex);
        kunex.setAdapter(new WordExAdapter(fragment.getActivity(), R.layout.view_listitem_furigana, details.kunex));
        ((CustomizableArrayAdapter) kunex.getAdapter()).setViewStyler(linkStyler);
        kunex.setOnItemClickListener(new LinkListener(fragment, details.kunex));

        FixedListView onex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_onex);
        onex.setAdapter(new WordExAdapter(fragment.getActivity(), R.layout.view_listitem_furigana, details.onex));
        ((CustomizableArrayAdapter) onex.getAdapter()).setViewStyler(linkStyler);
        onex.setOnItemClickListener(new LinkListener(fragment, details.onex));

        return view;

    }

    @Override
    public void save(Bundle outState) {
        outState.putString("nm_DETAILS_CLASS", this.getClass().getCanonicalName());
        outState.putSerializable("nm_DETAILS_DATA", details);
    }
}
