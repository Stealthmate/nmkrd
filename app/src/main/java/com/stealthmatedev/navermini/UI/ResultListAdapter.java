package com.stealthmatedev.navermini.UI;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.state.DetailedItem;
import com.stealthmatedev.navermini.state.SearchEngine;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class ResultListAdapter extends ArrayAdapter<DetailedItem> {

    public static class SerializableRepresentation implements Serializable {

        private final Class<? extends ResultListAdapter> childClass;
        private final String query;
        private final ArrayList<DetailedItem> results;
        private final boolean noMore;
        private final int page;

        private SerializableRepresentation(Class<? extends ResultListAdapter> childClass, String query, ArrayList<DetailedItem> results, int page, boolean noMore) {
            this.childClass = childClass;
            this.query = query;
            this.results = results;
            this.page = page;
            this.noMore = noMore;
        }
    }

    public static ResultListAdapter deserialize(StateManager state, SerializableRepresentation repr) {
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

    private static final int AD_POSITION = 4;
    private static final int PAGE_SIZE = 10;

    protected final StateManager state;
    private final String query;
    private int page;

    private boolean noMoreAvailable;
    private boolean loading;

    public ResultListAdapter(StateManager state, String query, String response) {
        super(state.getActivity(), 0, new ArrayList<DetailedItem>());
        this.addAll(parseResult(response));
        this.noMoreAvailable = super.getCount() == 0;
        this.state = state;
        this.query = query;
        this.page = 1;
        this.loading = false;
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
    }

    protected abstract ArrayList<DetailedItem> parseResult(String result);

    protected abstract View generateItem(int position, View convertView, ViewGroup parent);


    private View generateAd(ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_ad, parent, false);
        AdView ad = state.getActivity().resultListBannerAd();
        if (ad.getParent() != null) ((ViewGroup) ad.getParent()).removeView(ad);
        view.addView(ad);
        return view;
    }

    @NonNull
    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (position == getCount() - 1) {

            if(!noMoreAvailable)
            {
                if (loading) {
                    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_loading, parent, false);
                    v.setVisibility(View.VISIBLE);
                    return v;
                } else
                    return LayoutInflater.from(getContext()).inflate(R.layout.view_result_final, parent, false);
            } else {
                if(AD_POSITION > super.getCount() - 1) return generateAd(parent);
            }
        }

        if (position == AD_POSITION) return generateAd(parent);

        int actualPosition = position;

        if (position > AD_POSITION) actualPosition -= 1;

        return generateItem(actualPosition, convertView, parent);
    }

    @Override
    public final int getCount() {
        if(super.getCount() == 0) return 0;
        if (noMoreAvailable) return super.getCount() + 1;
        else return super.getCount() + 2;
    }

    private void setNoMoreAvailable(boolean b) {

        this.noMoreAvailable = b;
        notifyDataSetChanged();
    }

    public final boolean onItemClicked(View view, int position, long id) {
        if (position == getCount() - 1) {
            if (!noMoreAvailable) loadMoreIfAvailable();
        } else {
            state.loadDetails(getItem(position));
        }

        return true;
    }

    private void setLoading(boolean b) {
        this.loading = b;
        this.notifyDataSetChanged();
    }

    private void loadMoreIfAvailable() {
        state.query(query, page + 1, PAGE_SIZE, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                setLoading(false);
                ArrayList<DetailedItem> new_entries = parseResult(response);
                if (new_entries.size() < PAGE_SIZE) {
                    setNoMoreAvailable(true);
                }
                ResultListAdapter.this.addAll(new_entries);
                page++;
            }
        });
        setLoading(true);
    }

    public String getQuery() {
        return query;
    }

    public Serializable getDataRepresentation() {
        ArrayList<DetailedItem> results = new ArrayList<>(super.getCount());
        for (int i = 0; i <= super.getCount() - 1; i++) {
            results.add(getItem(i));
        }

        return new SerializableRepresentation(this.getClass(), query, results, page, noMoreAvailable);
    }
}
