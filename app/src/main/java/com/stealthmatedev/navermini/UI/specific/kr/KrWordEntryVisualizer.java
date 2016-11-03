package com.stealthmatedev.navermini.UI.specific.kr;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.StyledEntryVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.state.ResultListQuery;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class KrWordEntryVisualizer extends StyledEntryVisualizer {

    private static void setDefinition(Context context, View root, KrWord.Definition def) {

        ListView ex = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(context, R.layout.view_listitem_text_wide, def.ex));
    }

    private class DefinitionsAdapter extends ArrayAdapter<String> {

        private DefinitionsAdapter(Context context, ArrayList<KrWord.Definition> defs) {
            super(context, R.layout.view_listitem_text_wide);
            for (KrWord.Definition d : defs) {
                this.add(d.def);
            }
        }
    }

    private String highlight;

    public KrWordEntryVisualizer() {
        this.highlight = null;
    }

    public KrWordEntryVisualizer(ResultListQuery query) {
        this.highlight = query.query;
    }

    @Override
    public View visualize(Entry entry, ViewGroup parent) {

        final KrWord word = (KrWord) entry;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kr_word, parent, false);

        TextView name = (TextView) view.findViewById(R.id.kr_word_name);
        name.setText(word.word);

        TextView hanja = (TextView) view.findViewById(R.id.kr_word_hanja);
        hanja.setText(word.hanja);

        TextView pronun = (TextView) view.findViewById(R.id.kr_word_pronun);
        pronun.setText(word.pronun);

        TextView wordclass = (TextView) view.findViewById(R.id.kr_word_class);
        String classes = word.wclass;
        if (classes.equals("[]")) classes = "";
        wordclass.setText(classes);

        TextView meaning = (TextView) view.findViewById(R.id.kr_word_meaning);
        meaning.setText(word.defs.get(0).def);

        return view;
    }


    public View visualizeFull(Entry entry, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_generic_detail_word, parent, false);

        final KrWord details = (KrWord) entry;

        if(details == null) return view;

        TextView name = (TextView) view.findViewById(R.id.view_generic_detail_word_word);
        name.setText(details.word);

        TextView extra = (TextView) view.findViewById(R.id.view_generic_detail_word_extra);

        String hanjaStr = details.hanja;
        String pronunStr = details.pronun;
        StringBuilder extraStr = new StringBuilder();
        if (hanjaStr.length() > 0) {
            extraStr = extraStr.append(hanjaStr);
            if (pronunStr.length() > 0) extraStr = extraStr.append(" : ").append(pronunStr);
        } else if (pronunStr.length() > 0) extraStr = extraStr.append(pronunStr);

        extra.setText(extraStr);

        ListView deflist = (ListView) view.findViewById(R.id.view_generic_detail_word_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        final DefinitionsAdapter adapter = new DefinitionsAdapter(parent.getContext(), details.defs);
        deflist.setAdapter(adapter);

        deflist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setDefinition(parent.getContext(), parent.getRootView(), details.defs.get(position));
            }
        });

        KrWord.Definition def = details.defs.get(0);

        ListView ex = (ListView) view.findViewById(R.id.view_generic_detail_word_defex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(parent.getContext(), R.layout.view_listitem_text_wide, def.ex));

        return view;
    }
}
