package com.stealthmatedev.navermini.UI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdView;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.history.HistoryEntry;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;
/*
public abstract class DetailedItemListAdapter extends BaseListAdapter {

    protected static class SerializableRepresentation extends BaseListAdapter.SerializableRepresentation {

        private final ResultListQuery query;
        private final boolean noMore;
        private final int page;

        private SerializableRepresentation(Class<? extends DetailedItemListAdapter> childClass, ResultListQuery query, ArrayList<Entry> results, int page, boolean noMore) {
            super(childClass, results);
            this.query = query;
            this.page = page;
            this.noMore = noMore;
        }
    }

    private static final int PAGE_SIZE = 10;

    private ResultListQuery query;
    private int page;

    private boolean noMoreAvailable;
    private boolean loading;

    private ArrayList<DetailedItem> items;

    public DetailedItemListAdapter(StateManager state, ResultListQuery query, ArrayList<DetailedItem> items) {
        super(state);
        this.items = new ArrayList<>();
        for(DetailedItem i : items) this.items.add(i);
        this.noMoreAvailable = super.getCount() == 0;
        this.query = query;
        this.page = 1;
        this.loading = false;
    }

    public DetailedItemListAdapter(StateManager state, Serializable data) {
        super(state, new ArrayList<DetailedItem>());
        SerializableRepresentation desData = (SerializableRepresentation) data;
        this.noMoreAvailable = desData.noMore || desData.results.size() == 0;
        this.query = desData.query;
        this.page = desData.page;
        this.loading = false;
    }


    private void setLoading(boolean b) {
        this.loading = b;
        this.notifyDataSetChanged();
    }

    public ResultListQuery getQuery() {
        return query;
    }

    public Serializable getDataRepresentation() {
        ArrayList<Entry> results = new ArrayList<>(super.getCount());
        for (int i = 0; i <= super.getCount() - 1; i++) {
            results.add(getItem(i));
        }

        return new SerializableRepresentation(this.getClass(), query, results, page, noMoreAvailable);
    }

    private void loadMoreIfAvailable() {

        final ResultListQuery newQuery = new ResultListQuery(query.path, query.query, query.page + 1, query.pagesize);

        state.getSearchEngine().queryResultList(newQuery, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                setLoading(false);
                ArrayList<DetailedItem> new_entries = parseResult(response);

                boolean same = new_entries.get(new_entries.size() - 1).equals(getItem(DetailedItemListAdapter.super.getCount() - 1));

                if (same) {
                    setNoMoreAvailable(true);
                    return;
                }

                if (new_entries.size() < PAGE_SIZE) {
                    setNoMoreAvailable(true);
                }
                DetailedItemListAdapter.this.addAll(new_entries);
                query = newQuery;
            }

            @Override
            public void onError(VolleyError err) {
                setLoading(false);
            }
        });
        setLoading(true);
    }

    private View generateAd(ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_ad, parent, false);
        AdView ad = state.getActivity().resultListBannerAd();
        if (ad.getParent() != null) ((ViewGroup) ad.getParent()).removeView(ad);
        view.addView(ad);
        return view;
    }

    private void setNoMoreAvailable(boolean b) {

        this.noMoreAvailable = b;
        notifyDataSetChanged();
    }


    @Override
    protected View generateItem(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1 && !noMoreAvailable) {

            if (loading) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading, parent, false);
                v.setVisibility(View.VISIBLE);
                return v;
            } else
                return LayoutInflater.from(getContext()).inflate(R.layout.view_result_final, parent, false);
        }

        return generateItem(position, convertView, parent);
    }

    @Override
    protected boolean onClick(int position) {
        if (position == getCount() - 1) {
            if (!noMoreAvailable) loadMoreIfAvailable();
            return true;
        }

        final DetailedItem item = (DetailedItem) getItem(position);

        if (item == null) {
            Log.e(APPTAG, "Null item in result list at position " + position);
            return false;
        }

        Log.i(APPTAG, new HistoryEntry(item).getJson());

        final DetailsFragment dfrag = state.openDetailsPage();
        final DetailsVisualizer visualizer = getDetailsVisualizer(item);
        visualizer.populate(item);

        if (item.hasDetails()) {
            dfrag.waitForData();
            state.getSearchEngine().queryDetails(item.getLinkToDetails(), new SearchEngine.OnResponse() {
                @Override
                public void responseReady(String response) {
                    visualizer.populate(new DetailedItem.Translator(item.getClass()).translate(response));
                    dfrag.populate(visualizer);
                }

                @Override
                public void onError(VolleyError err) {
                    state.closePage(dfrag);
                }
            });
        } else {
            dfrag.populate(visualizer);
        }

        return true;
    }

    protected abstract ArrayList<DetailedItem> parseResult(String result);

    protected abstract DetailsVisualizer getDetailsVisualizer(DetailedItem item);

    protected abstract Class<? extends DetailedItem> getItemClass(DetailedItem item);

}
*/