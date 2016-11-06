package com.stealthmatedev.navermini.UI;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;

import static android.R.attr.entries;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class NetworkEntryListAdapter extends EntryListAdapter {

    private static class StateData implements Serializable {
        private final ResultListQuery query;
        private final boolean hasMore;

        private StateData(ResultListQuery query, boolean hasMore) {
            this.query = query;
            this.hasMore = hasMore;
        }
    }

    private ResultListQuery query;
    private StateManager state;

    private boolean hasMore;
    private boolean loading;

    public NetworkEntryListAdapter(StateManager state, SerializableRepresentation repr) {
        super(repr);
        StateData data = (StateData) repr.data;
        this.hasMore = data.hasMore;
        this.query = data.query;
        this.state = state;

    }

    public NetworkEntryListAdapter(StateManager state, ArrayList<Entry> entries, ResultListQuery query) {
        super(new ArrayListEntryProvider(entries));
        this.query = query;
        this.state = state;
        this.hasMore = true;
        this.loading = false;
    }

    @Override
    public int getCount() {
        return super.getCount() + (hasMore ? 1 : 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1 && hasMore) {
            if (!loading)
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_result_final, parent, false);
            else
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_loading, parent, false);
        } else return super.getView(position, convertView, parent);
    }

    @Override
    public boolean onItemClicked(int position) {

        if (position == getCount() - 1 && hasMore) {
            final ResultListQuery newQuery = new ResultListQuery(query.path, query.query, query.page + 1, query.pagesize, query.getSubDictionary());
            this.loading = true;
            notifyDataSetChanged();
            state.getSearchEngine().queryResultList(query, new SearchEngine.OnResponse() {
                @Override
                public void responseReady(String response) {
                    query = newQuery;
                    ArrayListEntryProvider provider = (ArrayListEntryProvider) getEntryProvider();
                    loading = false;
                    ArrayList<Entry> entries = query.getSubDictionary().translator.translate(response);

                    for (int i = 0; i < provider.getCount(); i++) {
                        if (provider.getEntry(i).equals(entries.get(0))) {
                            hasMore = false;
                            notifyDataSetChanged();
                            return;
                        }
                    }
                    if (entries.size() < query.pagesize) hasMore = false;
                    provider.addAll(entries);
                }

                @Override
                public void onError(VolleyError err) {
                    loading = false;
                    notifyDataSetChanged();
                }
            });
        }


        final Entry item = (Entry) getItem(position);
        if (item instanceof DetailedEntry) state.openDetails((DetailedEntry) item, true);
        return true;
    }

    @Override
    public Object getItem(int position) {
        if (position == getCount() - 1 && hasMore) return null;
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        if (position == getCount() - 1 && hasMore) return position;
        return super.getItemId(position);
    }

    @Override
    protected Serializable serialize() {
        return new StateData(query, hasMore);
    }
}
