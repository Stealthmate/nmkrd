package com.stealthmateoriginal.navermini.UI.kr;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;
import com.stealthmateoriginal.navermini.data.kr.KrWordDetails;
import com.stealthmateoriginal.navermini.data.kr.worddetails.Definition;
import com.stealthmateoriginal.navermini.data.kr.worddetails.WordDetails;
import com.stealthmateoriginal.navermini.state.StateManager;
import com.stealthmateoriginal.navermini.data.kr.KrWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class KrDetailsAdapter extends DetailsAdapter {

    private class DefinitionsAdapter extends ArrayAdapter<Definition> {

        public DefinitionsAdapter(Context context, ArrayList<Definition> defs) {
            super(context, R.layout.view_detail_kr_defitem, defs);
        }

        @Override
        public final View getView(final int position, View convertView, final ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_kr_defitem, parent, false);
            }
            
            ((TextView)convertView).setText(getItem(position).def);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDefinition(getItem(position));
                }
            });

            return convertView;
        }
    }

    private WordDetails details;

    public KrDetailsAdapter(DetailsFragment fragment, String response) {
        super(fragment);
        try {
            this.details = new WordDetails(response);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }
    }

    private void setDefinition(Definition def) {

        View root = fragment.getView();

        TextView head = (TextView) root.findViewById(R.id.view_kr_detail_definition_head);
        head.setText(def.def);

        ListView ex = (ListView) root.findViewById(R.id.view_kr_detail_definition_ex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(fragment.getActivity(), R.layout.view_kr_detail_definition_example, R.id.viewid_kr_detail_def_ex_text, def.ex));
    }

    @Override
    public View getView(ViewGroup container) {

        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.layout_kr_detail, container, false);

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

        DefinitionsAdapter adapter = new DefinitionsAdapter(fragment.getActivity(), details.defs);
        deflist.setAdapter(adapter);

        Definition def = details.defs.get(0);

        TextView head = (TextView) view.findViewById(R.id.view_kr_detail_definition_head);
        head.setText(def.def);

        ListView ex = (ListView) view.findViewById(R.id.view_kr_detail_definition_ex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(fragment.getActivity(), R.layout.view_kr_detail_definition_example, R.id.viewid_kr_detail_def_ex_text, def.ex));

        return view;
    }

    @Override
    public void save(Bundle outState) {
        outState.putString("nm_DETAILS_CLASS", this.getClass().getCanonicalName());
        outState.putSerializable("nm_DETAILS_DATA", details);
    }
}
