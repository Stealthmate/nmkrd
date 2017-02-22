package com.stealthmatedev.navermini.UI.specific.hj.details;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.generic.ListLayout;
import com.stealthmatedev.navermini.UI.generic.UIUtils;
import com.stealthmatedev.navermini.data.hj.HjHanja;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Stealthmate on 16/12/08 0008.
 */

public class HjHanjaDetailsVisualizer extends DetailsVisualizer {

    private static class RelatedHanjaAdapter extends BaseAdapter {

        enum Category {
            DIFF_FORM(R.string.label_hj_relhj_diffForm),
            REL_SHAPE(R.string.label_hj_relhj_relShape),
            REL_MEAN(R.string.label_hj_relhj_relMean),
            OPP_MEAN(R.string.label_hj_relhj_oppMean);

            public final int displayName;

            Category(int displayName) {
                this.displayName = displayName;
            }

        }

        private final int count;
        private final ArrayList<Pair<Category, List<String>>> items;

        RelatedHanjaAdapter(HjHanja.RelatedHanja relhj) {
            items = new ArrayList<>();

            if (relhj.diffForm.size() > 0)
                items.add(new Pair<Category, List<String>>(Category.DIFF_FORM, relhj.diffForm));
            if (relhj.relShape.size() > 0)
                items.add(new Pair<Category, List<String>>(Category.REL_SHAPE, relhj.relShape));
            if (relhj.relMean.size() > 0)
                items.add(new Pair<Category, List<String>>(Category.REL_MEAN, relhj.relMean));
            if (relhj.oppMean.size() > 0)
                items.add(new Pair<Category, List<String>>(Category.OPP_MEAN, relhj.oppMean));

            this.count = items.size();
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hj_detail_relhj, parent, false);

            Pair<Category, List<String>> item = (Pair<Category, List<String>>) getItem(position);

            TextView category = (TextView) convertView.findViewById(R.id.view_hj_detail_relhj_category);
            category.setText(item.first.displayName);

            ListLayout list = (ListLayout) convertView.findViewById(R.id.view_hj_detail_relhj_list);
            list.clear();
            UIUtils.populateViewGroup(list, new ArrayAdapter<String>(parent.getContext(), R.layout.view_listitem_minimal, item.second) {
                @NonNull
                @Override
                public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.highlight_selector));
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    int padding = (int) UIUtils.dp(getContext(), 5);
                    v.setTextColor(ContextCompat.getColor(getContext(), R.color.nm_colorTextAccent));
                    v.setPadding(2 * padding, padding, 2 * padding, padding);
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv = (TextView) v;
                            StateManager.getState().openDetails(new HjHanja((String) tv.getText().subSequence(0, 1)), true);
                        }
                    });
                    return v;
                }
            });

            return convertView;
        }
    }

    private static class RelatedWordAdapter extends ArrayAdapter<HjHanja.HanjaWord> {

        public RelatedWordAdapter(Context context, ArrayList<HjHanja.HanjaWord> words) {
            super(context, 0, words);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_minimal, parent, false);

            TextView text = ((TextView) convertView);

            HjHanja.HanjaWord hjw = getItem(position);

            text.setText(hjw.hj + " - " + hjw.hg);

            return text;
        }
    }


    private static String join(ArrayList<String> arr, String delim) {
        String str = "";
        for (int i = 0; i < arr.size(); i++) {
            str += arr.get(i);
            if (i < arr.size() - 1) str += delim;
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


        FlexboxLayout strokediag = (FlexboxLayout) view.findViewById(R.id.view_hj_detail_stroke_diag);
        Context context = container.getContext();
        for (int i = 0; i < details.strokeDiagram.size(); i++) {

            ImageView img = new ImageView(context);
            float size = UIUtils.dp(context, 75);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams((int) size, (int) size);
            lp.setMargins(0, 0, (int) UIUtils.dp(context, 10), 0);
            img.setLayoutParams(lp);
            strokediag.addView(img);
            Picasso.with(container.getContext()).load(details.strokeDiagram.get(i)).into(img);
        }

        ListView meanings = (ListView) view.findViewById(R.id.view_hj_detail_hanja_meanings);
        meanings.setAdapter(new ArrayAdapter<>(containerFragment.getContext(), R.layout.view_listitem_minimal, details.meanings));
        containerFragment.registerForContextMenu(meanings);

        TextView expl = (TextView) view.findViewById(R.id.view_hj_detail_expl);
        if (details.expl.size() > 0) {
            ((View) expl.getParent()).setVisibility(View.VISIBLE);
            expl.setText(join(details.expl, "\n"));
        } else {
            ((View) expl.getParent()).setVisibility(GONE);
        }

        TextView glyphExpl = (TextView) view.findViewById(R.id.view_hj_detail_glyphexpl);
        if (details.glyphexpl.length() > 0) {
            ((View) glyphExpl.getParent()).setVisibility(View.VISIBLE);
            glyphExpl.setText(details.glyphexpl);
        } else {
            ((View) glyphExpl.getParent()).setVisibility(GONE);
        }

        TextView reference = (TextView) view.findViewById(R.id.view_hj_detail_reference);
        if (details.reference.length() > 0) {
            ((View) reference.getParent()).setVisibility(View.VISIBLE);
            reference.setText(details.reference);
        } else {
            ((View) reference.getParent()).setVisibility(GONE);
        }

        ListLayout relHanja = (ListLayout) view.findViewById(R.id.view_hj_detail_list_relhanja);
        if (details.relHanja.hasAny()) {
            relHanja.populate(new RelatedHanjaAdapter(details.relHanja));
            ((ViewGroup) relHanja.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup) relHanja.getParent()).setVisibility(View.GONE);
        }

        ListView relWords = (ListView) view.findViewById(R.id.view_hj_detail_list_relwords);
        if (details.relWords.size() > 0) {
            relWords.setAdapter(new RelatedWordAdapter(containerFragment.getContext(), details.relWords));
            ((ViewGroup) relWords.getParent()).setVisibility(View.VISIBLE);
            containerFragment.registerForContextMenu(relWords);
        } else {
            ((ViewGroup) relWords.getParent()).setVisibility(View.GONE);
        }

        ListView relIdioms = (ListView) view.findViewById(R.id.view_hj_detail_list_relidioms);
        if (details.relIdioms.size() > 0) {
            relIdioms.setAdapter(new RelatedWordAdapter(containerFragment.getContext(), details.relIdioms));
            ((ViewGroup) relIdioms.getParent()).setVisibility(View.VISIBLE);
            containerFragment.registerForContextMenu(relIdioms);
        } else {
            ((ViewGroup) relIdioms.getParent()).setVisibility(View.GONE);
        }


        return view;
    }

    @Override
    public void onCreateContextMenu(Fragment containerFragment, ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        ListView lv = ((ListView) view);
        int menuid = lv.getId();
        switch (menuid) {
            case R.id.view_hj_detail_list_relidioms:
            case R.id.view_hj_detail_list_relwords: {
                menu.add(Menu.NONE, menuid, 0, R.string.label_menu_copy_hanja).setActionView(lv);
                menu.add(Menu.NONE, menuid, 1, R.string.label_menu_copy_hangeul).setActionView(lv);
            }
            break;
            default: {
                menu.add(Menu.NONE, menuid, 0, android.R.string.copy).setActionView(lv);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(Fragment containerFragment, MenuItem menuItem) {
        String text = null;

        int position = ((AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo()).position;

        ListView lv = (ListView) menuItem.getActionView();
        Object item = lv.getAdapter().getItem(position);

        switch (menuItem.getItemId()) {
            case R.id.view_hj_detail_hanja_meanings: {
                text = (String) item;
            }
            break;
            case R.id.view_hj_detail_list_relidioms:
            case R.id.view_hj_detail_list_relwords: {
                switch (menuItem.getGroupId()) {
                    case 0:
                        text = ((HjHanja.HanjaWord) item).hj;
                        break;
                    case 1:
                        text = ((HjHanja.HanjaWord) item).hg;
                }
            }
        }

        ClipboardManager cbm = (ClipboardManager) containerFragment.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));

        Toast.makeText(containerFragment.getContext(), "Copied", Toast.LENGTH_SHORT).show();

        return true;
    }
}