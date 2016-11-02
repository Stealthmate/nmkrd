package com.stealthmatedev.navermini.UI.kr.details;

import android.animation.LayoutTransition;
import android.content.Context;
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
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.kr.KrWord;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class KrDetailsVisualizer extends DetailsVisualizer {

    private class DefinitionsAdapter extends ArrayAdapter<String> {

        private DefinitionsAdapter(Context context, ArrayList<KrWord.Definition> defs) {
            super(context, R.layout.view_listitem_text_wide);
            for (KrWord.Definition d : defs) {
                this.add(d.def);
            }
        }
    }

    private static void setDefinition(Context context, View root, KrWord.Definition def) {

        ListView ex = (ListView) root.findViewById(R.id.view_generic_detail_word_defex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(context, R.layout.view_listitem_text_wide, def.ex));
    }

    public KrDetailsVisualizer() {
        super();
    }

    public KrDetailsVisualizer(KrWord word) {
        super();
        this.populate(word);
    }

    @Override
    public View getView(final ViewGroup container) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_generic_detail_word, container, false);

        final KrWord details = (KrWord) getDetails();

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

        final DefinitionsAdapter adapter = new DefinitionsAdapter(container.getContext(), details.defs);
        deflist.setAdapter(adapter);

        deflist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setDefinition(container.getContext(), parent.getRootView(), details.defs.get(position));
            }
        });

        KrWord.Definition def = details.defs.get(0);

        ListView ex = (ListView) view.findViewById(R.id.view_generic_detail_word_defex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_text_wide, def.ex));

        return view;
    }

}
