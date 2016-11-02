package com.stealthmatedev.navermini.UI;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdView;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.history.HistoryEntry;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

public abstract class ResultListAdapter extends ArrayAdapter<DetailedItem> {

    protected static class SerializableRepresentation implements Serializable {

        private final Class<? extends ResultListAdapter> childClass;
        private final ResultListQuery query;
        private final ArrayList<DetailedItem> results;
        private final boolean noMore;
        private final int page;

        private SerializableRepresentation(Class<? extends ResultListAdapter> childClass, ResultListQuery query, ArrayList<DetailedItem> results, int page, boolean noMore) {
            this.childClass = childClass;
            this.query = query;
            this.results = results;
            this.page = page;
            this.noMore = noMore;
        }
    }

    static ResultListAdapter deserialize(StateManager state, SerializableRepresentation repr) {
        if (repr == null) return null;

        Class<? extends ResultListAdapter> _class = repr.childClass;
        if (_class == null) return null;

        try {
            return _class.getConstructor(StateManager.class, SerializableRepresentation.class).newInstance(state, repr);
        } catch (NoSuchMethodException e) {
            Log.e(APPTAG, "Cannot instantiate ResultListAdapter: Constructor from SerializableRepresentation not declared in " + _class.getSimpleName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static final int AD_POSITION = 3;
    private static final int PAGE_SIZE = 10;

    protected final StateManager state;
    private ResultListQuery query;
    private int page;

    private final int adPosition;

    private boolean noMoreAvailable;
    private boolean loading;

    public ResultListAdapter(StateManager state, ResultListQuery query, String response) {
        super(state.getActivity(), 0, new ArrayList<DetailedItem>());
        this.addAll(parseResult(response));
        this.noMoreAvailable = super.getCount() == 0;
        this.state = state;
        this.query = query;
        this.page = 1;
        this.loading = false;
        int items = super.getCount();

        this.adPosition = AD_POSITION > items - 1 ? items : AD_POSITION;
    }

    public ResultListAdapter(StateManager state, Serializable data) {
        super(state.getActivity(), 0, new ArrayList<DetailedItem>());
        SerializableRepresentation desData = (SerializableRepresentation) data;
        this.addAll(desData.results);
        this.state = state;
        this.noMoreAvailable = desData.noMore || desData.results.size() == 0;
        this.query = desData.query;
        this.page = desData.page;
        this.loading = false;
        int items = super.getCount();

        this.adPosition = AD_POSITION > items - 1 ? items : AD_POSITION;
    }


    private void setLoading(boolean b) {
        this.loading = b;
        this.notifyDataSetChanged();
    }

    public ResultListQuery getQuery() {
        return query;
    }

    public Serializable getDataRepresentation() {
        ArrayList<DetailedItem> results = new ArrayList<>(super.getCount());
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

                boolean same = new_entries.get(new_entries.size()-1).equals(getItem(ResultListAdapter.super.getCount()-1));

                if(same) {
                    setNoMoreAvailable(true);
                    return;
                }

                if (new_entries.size() < PAGE_SIZE) {
                    setNoMoreAvailable(true);
                }
                ResultListAdapter.this.addAll(new_entries);
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
    public final int getCount() {
        if (super.getCount() == 0) return 0;
        if (noMoreAvailable) return super.getCount() + 1;
        else return super.getCount() + 2;
    }


    @NonNull
    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (position == getCount() - 1 && !noMoreAvailable) {

            if (loading) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading, parent, false);
                v.setVisibility(View.VISIBLE);
                return v;
            } else
                return LayoutInflater.from(getContext()).inflate(R.layout.view_result_final, parent, false);
        }

        if (position == adPosition) return generateAd(parent);

        if (position > adPosition) position = position - 1;

        return generateItem(position, convertView, parent);
    }

    final boolean onItemClicked(int position) {
        if (position == getCount() - 1) {
            if (!noMoreAvailable) loadMoreIfAvailable();
        } else {

            if(position > adPosition) position -= 1;

            final DetailedItem item = getItem(position);

            if(item == null) {
                Log.e(APPTAG, "Null item in result list at position " + position);
                return false;
            }

            Log.i(APPTAG, new HistoryEntry(item).getJson());

            final DetailsFragment dfrag = state.openDetailsPage();
            final DetailsVisualizer visualizer = getDetailsVisualizer(item);
            visualizer.populate(item);

            if(item.hasDetails()) {
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
        }

        return true;
    }


    protected abstract ArrayList<DetailedItem> parseResult(String result);

    protected abstract View generateItem(int position, View convertView, ViewGroup parent);

    protected abstract DetailsVisualizer getDetailsVisualizer(DetailedItem item);

}
