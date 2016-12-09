package com.stealthmatedev.navermini.UI.specific.hj.details;

import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.hj.HjHanja;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/12/08 0008.
 */

public class HjHanjaDetailsVisualizer extends DetailsVisualizer {

    private static String join(ArrayList<String> arr, String delim) {
        String str = "";
        for(int i=0;i<arr.size();i++) {
            str += arr.get(i);
            if(i < arr.size() - 1) str += delim;
        }

        return str;
    }

    @Override
    public View getView(Fragment containerFragment, ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_hj_detail_hanja, container, false);

        HjHanja details = (HjHanja) getDetails();

        if (details == null) return view;

        TextView kanji = (TextView) view.findViewById(R.id.view_hj_hanja);
        kanji.setText(String.valueOf(details.hanja));

        TextView strokes = (TextView) view.findViewById(R.id.view_hj_hanja_strokes);
        strokes.setText(String.valueOf(details.strokes));

        TextView radical = (TextView) view.findViewById(R.id.view_hj_hanja_radical);
        radical.setText(String.valueOf(details.radical));

        TextView readings = (TextView) view.findViewById(R.id.view_hj_hanja_readings);
        readings.setText(join(details.readings, " | "));

        TextView saseongeum = (TextView) view.findViewById(R.id.view_hj_hanja_saseongeum);
        saseongeum.setText(join(details.saseongeum, ", "));

        TextView difficulty = (TextView) view.findViewById(R.id.view_hj_hanja_difficulty);
        difficulty.setText(details.difficulty);

        ListView meanings = (ListView) view.findViewById(R.id.view_hj_detail_hanja_meanings);
        meanings.setAdapter(new ArrayAdapter<>(containerFragment.getContext(), R.layout.view_listitem_minimal, details.meanings));

        TextView expl = (TextView) view.findViewById(R.id.view_hj_detail_expl);
        expl.setText(join(details.expl, "\n"));
        /*
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
            meanings.setAdapter(new JpKanjiDetailsVisualizer.KanjiMeaningsAdapter(containerFragment, details.meanings));
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

            kunex.setAdapter(new JpKanjiDetailsVisualizer.WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.kunex));
            ((CustomizableArrayAdapter) kunex.getAdapter()).setViewStyler(linkStyler);
            kunex.setOnItemClickListener(new JpKanjiDetailsVisualizer.LinkListener(container.getContext(), details.kunex));
            containerFragment.registerForContextMenu(kunex);
        }

        FixedListView onex = (FixedListView) view.findViewById(R.id.view_detail_jp_kanji_list_onex);
        if (details.onex.size() == 0) {

            onex.setVisibility(View.GONE);
            view.findViewById(R.id.view_jp_detail_kanji_label_onex).setVisibility(View.GONE);
        } else {
            onex.setAdapter(new JpKanjiDetailsVisualizer.WordExAdapter(container.getContext(), R.layout.view_listitem_furigana, details.onex));
            ((CustomizableArrayAdapter) onex.getAdapter()).setViewStyler(linkStyler);
            onex.setOnItemClickListener(new JpKanjiDetailsVisualizer.LinkListener(container.getContext(), details.onex));
            containerFragment.registerForContextMenu(onex);
        }
*/
        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

    }

    @Override
    public boolean onContextItemSelected(Fragment containerFragment, MenuItem item) {
        return false;
    }
}
