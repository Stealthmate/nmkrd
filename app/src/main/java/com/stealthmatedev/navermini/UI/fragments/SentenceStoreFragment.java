package com.stealthmatedev.navermini.UI.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stealthmatedev.navermini.EarliestStringMatchComparator;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.SentenceEntry;
import com.stealthmatedev.navermini.data.sentencestore.SentenceStoreManager;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public class SentenceStoreFragment extends Fragment {

    private ListView list;
    private View loadingView;
    private StateManager state;

    private class SentenceAdapter extends ArrayAdapter<SentenceEntry> {

        private class SentenceFilter extends Filter {


            ArrayList<SentenceEntry> entries;

            SentenceFilter(ArrayList<SentenceEntry> entries) {
                this.entries = entries;
            }

            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {

                FilterResults fr = new FilterResults();
                SortedSet<SentenceEntry> filteredEntries = new TreeSet<>(new Comparator<SentenceEntry>() {
                    @Override
                    public int compare(SentenceEntry o1, SentenceEntry o2) {
                        if(o1.keyword.equals(constraint) ^ o2.keyword.equals(constraint)) return o1.keyword.equals(constraint) ? -1 : +1;
                        return new EarliestStringMatchComparator(constraint.toString()).compare(o1.ex, o2.ex);
                    }
                });

                String match = constraint.toString().trim();
                for (SentenceEntry entry : entries) {
                    if ((entry.keyword.contains(match + " ") || entry.keyword.contains(" " + match))
                            || (entry.ex.contains(constraint))) {
                        filteredEntries.add(entry);
                    }
                }


                fr.values = new ArrayList<>(filteredEntries);
                fr.count = filteredEntries.size();
                return fr;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                SentenceAdapter adapter = SentenceAdapter.this;
                ArrayList<SentenceEntry> entries = (ArrayList<SentenceEntry>) results.values;
                adapter.clear();
                adapter.addAll(entries);
                adapter.notifyDataSetChanged();
                loadingView.setVisibility(View.GONE);
            }
        }

        private SentenceFilter filter;

        public SentenceAdapter(Context context, ArrayList<SentenceEntry> sentences) {
            super(context, 0);
            this.filter = new SentenceFilter(sentences);
            this.addAll(sentences);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            SentenceEntry item = getItem(position);

            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_sentence, parent, false);

            TextView cc = (TextView) convertView.findViewById(R.id.view_listitem_sentence_cc);
            cc.setText(item.from.code + " - " + item.to.code);

            TextView keyword = (TextView) convertView.findViewById(R.id.view_listitem_sentence_keyword);
            keyword.setText(item.keyword);

            TextView original = (TextView) convertView.findViewById(R.id.view_listitem_sentence_original);
            original.setText(item.ex);

            TextView translated = (TextView) convertView.findViewById(R.id.view_listitem_sentence_translated);
            if (item.tr.length() > 0) translated.setText(item.tr);
            else {
                translated.setVisibility(View.GONE);
            }

            return convertView;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return this.filter;
        }
    }

    private void waitForData() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void update() {
        state.sentenceStore().queryAll(new SentenceStoreManager.Callback() {
            @Override
            public void callback(Object result) {
                list.setAdapter(new SentenceAdapter(getContext(), (ArrayList<SentenceEntry>) result));
                loadingView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sentence_store, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        this.loadingView = this.getView().findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.VISIBLE);

        this.state = StateManager.getState(getContext());

        final EditText searchBox = (EditText) getView().findViewById(R.id.search_bar);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) return;
                ((ArrayAdapter) list.getAdapter()).getFilter().filter(s);
                waitForData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        this.list = (ListView) getView().findViewById(R.id.result);
        registerForContextMenu(this.list);

        update();


        this.state = StateManager.getState(getContext());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.add(Menu.NONE, 0, 1, "Copy sentence");
        String translated = ((SentenceEntry) this.list.getAdapter().getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position)).tr;
        if (translated != null && translated.length() > 0)
            menu.add(Menu.NONE, 1, 2, "Copy translated");

        menu.add(Menu.NONE, 2, 3, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {

        SentenceEntry entry = (SentenceEntry) this.list.getAdapter().getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);

        if (item.getItemId() == 3) {
            state.sentenceStore().remove(entry, new SentenceStoreManager.Callback() {
                @Override
                public void callback(Object result) {
                    update();
                }
            });
        }


        String text = null;
        if (item.getItemId() == 0)
            text = entry.ex;
        else if (item.getItemId() == 1)
            text = entry.tr;

        ClipboardManager cbm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, text));

        Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();

        return true;
    }

}
