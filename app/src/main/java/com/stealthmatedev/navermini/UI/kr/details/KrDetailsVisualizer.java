package com.stealthmatedev.navermini.UI.kr.details;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.kr.worddetails.Definition;
import com.stealthmatedev.navermini.data.kr.worddetails.WordDetails;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class KrDetailsVisualizer extends DetailsVisualizer {

    private class DefinitionsAdapter extends ArrayAdapter<Definition> {

        public DefinitionsAdapter(Context context, ArrayList<Definition> defs) {
            super(context, R.layout.view_detail_kr_defitem, defs);
        }

        @NonNull
        @Override
        public final View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_kr_defitem, parent, false);
            }
            
            ((TextView)convertView).setText(getItem(position).def);

            final View finalConvertView = convertView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDefinition(getContext(), parent.getRootView(), getItem(position));
                }
            });

            return convertView;
        }
    }

    private static void setDefinition(Context context, View root, Definition def) {

        TextView head = (TextView) root.findViewById(R.id.view_kr_detail_definition_head);
        head.setText(def.def);

        ListView ex = (ListView) root.findViewById(R.id.view_kr_detail_definition_ex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(context, R.layout.view_kr_detail_definition_example, R.id.viewid_kr_detail_def_ex_text, def.ex));
    }

    private WordDetails details;

    public KrDetailsVisualizer(Context context, String response) {
        super(context);
        try {
            this.details = new WordDetails(response);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON ERROR");
        }
    }

    public KrDetailsVisualizer(Context context, Serializable data) {
        super(context);
        this.details = (WordDetails) data;

    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_kr_detail, container, false);

        TextView name = (TextView) view.findViewById(R.id.kr_detail_word);
        name.setText(details.word.name);

        TextView hanja = (TextView) view.findViewById(R.id.kr_detail_hanja);
        hanja.setText(details.word.hanja);

        TextView pronun = (TextView) view.findViewById(R.id.kr_detail_pronun);
        pronun.setText(details.word.pronunciation);

        ListView deflist = (ListView) view.findViewById(R.id.view_detail_kr_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        DefinitionsAdapter adapter = new DefinitionsAdapter(context, details.defs);
        deflist.setAdapter(adapter);

        Definition def = details.defs.get(0);

        TextView head = (TextView) view.findViewById(R.id.view_kr_detail_definition_head);
        head.setText(def.def);

        ListView ex = (ListView) view.findViewById(R.id.view_kr_detail_definition_ex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(context, R.layout.view_kr_detail_definition_example, R.id.viewid_kr_detail_def_ex_text, def.ex));

        return view;
    }

    @Override
    public Serializable getDataRepresentation(){
        return details;
    }
}
