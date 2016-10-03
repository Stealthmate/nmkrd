package com.stealthmateoriginal.navermini.UI.kr;

import android.animation.LayoutTransition;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.ExpandableListLayout;
import com.stealthmateoriginal.navermini.state.StateManager;
import com.stealthmateoriginal.navermini.state.kr.KrWordEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public class KrDetailsAdapter extends DetailsAdapter {

    private class Definition {

        private String def;
        private ArrayList<String> ex;

        public Definition(String def, ArrayList<String> ex) {
            this.def = def;
            this.ex = ex;
        }

    }

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

    private KrWordEntry word;
    private ArrayList<Definition> defs;


    public KrDetailsAdapter(StateManager state, KrWordEntry word, String response) {
        super(state, response);
        this.word = word;
        try {
            this.defs = new ArrayList<>(parseResponse(response));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }
    }

    private List<Definition> parseResponse(String response) throws JSONException {
        JSONArray defarr = new JSONArray(response);

        ArrayList<Definition> defs = new ArrayList<>(defarr.length());

        for (int i = 0; i <= defarr.length() - 1; i++) {
            JSONObject obj = defarr.getJSONObject(i);
            String def = obj.getString("def");

            JSONArray exarr = obj.getJSONArray("ex");
            ArrayList<String> ex = new ArrayList<>(exarr.length());
            for (int j = 0; j <= exarr.length() - 1; j++) {
                ex.add(exarr.getString(j));
            }

            defs.add(new Definition(def, ex));
        }

        return defs;
    }

    public void setDefinition(Definition def) {

        ViewGroup root = (ViewGroup) state.getDetailsFragment().getView();
        TextView head = (TextView) root.findViewById(R.id.view_kr_detail_definition_head);
        head.setText(def.def);

        ListView ex = (ListView) root.findViewById(R.id.view_kr_detail_definition_ex_list);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(state.getActivity(), R.layout.view_kr_detail_definition_example, R.id.viewid_kr_detail_def_ex_text, def.ex));
    }

    @Override
    public void populateContainer(View container) {

        ViewGroup.inflate(state.getActivity(), R.layout.layout_kr_detail, (ViewGroup) container);

        TextView name = (TextView) container.findViewById(R.id.kr_detail_word);
        name.setText(word.getName());

        TextView hanja = (TextView) container.findViewById(R.id.kr_detail_hanja);
        hanja.setText(word.getHanja());

        TextView pronun = (TextView) container.findViewById(R.id.kr_detail_pronun);
        pronun.setText(word.getPronunciation());

        LinearLayout top = (LinearLayout) container.findViewById(R.id.viewid_kr_detail_def_container);

        final ListView deflist = (ListView) container.findViewById(R.id.view_detail_kr_deflist);
        deflist.removeAllViewsInLayout();
        deflist.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        deflist.getLayoutTransition().setDuration(100);

        final DefinitionsAdapter adapter = new DefinitionsAdapter(state.getActivity(), defs);
        deflist.setAdapter(adapter);

        setDefinition(adapter.getItem(0));
    }
}
