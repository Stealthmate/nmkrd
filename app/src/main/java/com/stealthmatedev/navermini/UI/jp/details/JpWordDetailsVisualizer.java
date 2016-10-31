package com.stealthmatedev.navermini.UI.jp.details;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.TranslatedExampleAdapter;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public class JpWordDetailsVisualizer extends DetailsVisualizer {

    private static class WordclassAdapter extends ArrayAdapter<JpWord.WordClassGroup> {

        private JpWord details;

        WordclassAdapter(Context context, JpWord details) {
            super(context, 0, new ArrayList<>(details.clsgrps));
            this.details = details;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_wclass, parent, false);
            }

            JpWord.WordClassGroup item = getItem(position);

            if (item == null) return convertView;

            final String wordclass = item.wclass;

            TextView wordclassView = (TextView) convertView.findViewById(R.id.view_listitem_wclass_class);
            wordclassView.setText(wordclass);

            ListView meanings = (ListView) convertView.findViewById(R.id.view_listitem_wclass_deflist);

            ArrayList<Meaning> meaningsarr = item.meanings;
            ArrayList<String> meaningStrArr = new ArrayList<>(meaningsarr.size());
            if (meaningsarr.size() > 1) {
                for (Meaning m : meaningsarr) {
                    if (m.m.length() > 0) meaningStrArr.add(m.m);
                    else {
                        for (Meaning.Gloss g : m.glosses) meaningStrArr.add(g.g);
                    }
                }
            } else {
                for (Meaning.Gloss g : meaningsarr.get(0).glosses) {
                    meaningStrArr.add(g.g);
                }
            }

            meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_furigana, meaningStrArr));
            meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int meaningPosition, long id) {
                    setDefinition(getContext(), parent.getRootView(), details.clsgrps.get(position).meanings.get(meaningPosition));
                }
            });

            return convertView;
        }

        /**
         * Created by Stealthmate on 16/10/05 0005.
         */

        static class GlossAdapter extends ArrayAdapter<Meaning.Gloss> {
            GlossAdapter(Context context, ArrayList<Meaning.Gloss> glosses) {
                super(context, 0, glosses);
            }

            @Override
            public int getCount() {
                if (super.getCount() > 1) return super.getCount() - 1;
                return super.getCount();
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

                if (super.getCount() > 1) position = position + 1;

                if (convertView == null)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_jp_detail_definition_meaning_gloss, parent, false);

                Meaning.Gloss item = getItem(position);

                if(item == null) return convertView;

                TextView gloss = (TextView) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss);
                gloss.setText(item.g);
                if (gloss.getText().length() == 0) gloss.setVisibility(GONE);
                else gloss.setVisibility(View.VISIBLE);

                ListLayout ex = (ListLayout) convertView.findViewById(R.id.view_jp_detail_definition_meaning_gloss_ex);
                ex.clear();
                ex.populate(new TranslatedExampleAdapter(getContext(), item.ex));

                return convertView;
            }
        }
    }

    private JpWord details;

    public JpWordDetailsVisualizer() {
        super();
    }

    public JpWordDetailsVisualizer(JpWord word) {
        super();
        this.details = word;
    }

    public JpWordDetailsVisualizer(Serializable data) {
        super();
        throw new UnsupportedOperationException("JpWordDetailsVisualizer: constructor from serialized data not yet supported.");
        //this.details = new KrWord(data);
    }

    private static void setDefinition(Context context, View root, Meaning meaning) {

        TextView meaningView = (TextView) root.findViewById(R.id.view_jp_detail_word_definition);
        meaningView.setText(meaning.m);

        ListView glossList = (ListView) root.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new WordclassAdapter.GlossAdapter(context, meaning.glosses));
    }

    @Override
    public void populate(String data) {
        this.details = new Gson().fromJson(data, JpWord.class);
    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_jp_detail_word, container, false);

        TextView name = (TextView) view.findViewById(R.id.view_jp_detail_word_name);
        name.setText(details.word);

        TextView kanji = (TextView) view.findViewById(R.id.view_jp_detail_word_kanji);
        kanji.setText(details.kanji);

        final ListView deflist = (ListView) view.findViewById(R.id.view_jp_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        WordclassAdapter adapter = new WordclassAdapter(container.getContext(), details);
        deflist.setAdapter(adapter);

        JpWord.WordClassGroup.Meaning meaning = details.clsgrps.get(0).meanings.get(0);

        TextView meaningView = (TextView) view.findViewById(R.id.view_jp_detail_word_definition);
        String meanstr = meaning.m;
        if (meanstr.length() == 0) meanstr = meaning.glosses.get(0).g;
        meaningView.setText(meanstr);

        ListView glossList = (ListView) view.findViewById(R.id.view_jp_detail_word_definition_gloss);
        glossList.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        glossList.setAdapter(new WordclassAdapter.GlossAdapter(container.getContext(), meaning.glosses));

        return view;
    }

    @Override
    public Serializable getDataRepresentation() {
        return details;
    }
}
