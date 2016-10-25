package com.stealthmateoriginal.navermini.UI.jp;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.ExpandableListLayout;
import com.stealthmateoriginal.navermini.UI.kr.KrDetailsAdapter;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */
/*
public class JpDetailsAdapter extends DetailsAdapter {
    @Override
    public void populate(View container) {
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

        final KrDetailsAdapter.DefinitionsAdapter adapter = new KrDetailsAdapter.DefinitionsAdapter(state.getActivity(), defs);
        deflist.setAdapter(adapter);

        setDefinition(adapter.getItem(0));

        Button clps = (Button) container.findViewById(R.id.view_kr_detail_btn_collapseAll);
        clps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <= deflist.getChildCount() - 1l; i++) {
                    ((ExpandableListLayout) deflist.getChildAt(i).findViewById(R.id.view_kr_detail_definition_ex_list)).toggle(true);
                }
            }
        });

    }
}
*/