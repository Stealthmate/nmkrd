package com.stealthmatedev.navermini.UI.specific.kr.details;

import android.animation.LayoutTransition;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.kr.KrWord;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

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

    private void setDefinition(Context context, View root, KrWord.Definition def) {

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
    public View getView(Fragment containerFragment, final ViewGroup container) {

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
        containerFragment.registerForContextMenu(deflist);
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
        containerFragment.registerForContextMenu(ex);
        ex.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        ex.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.view_listitem_text_wide, def.ex));

        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        int menuid = view.getId();
        menu.add(Menu.NONE, menuid, 0, android.R.string.copy).setActionView(view);
    }

    @Override
    public boolean onContextItemSelected(Fragment containerFragment, MenuItem menuItem) {
        int position = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;

        ListView lv = (ListView) menuItem.getActionView();

        String text = (String) lv.getAdapter().getItem(position);

        ClipboardManager cbm = (ClipboardManager) containerFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));

        Log.d(APPTAG, "Copied " + text);
        Toast.makeText(containerFragment.getContext(), "Copied", Toast.LENGTH_SHORT).show();

        return true;
    }

}
