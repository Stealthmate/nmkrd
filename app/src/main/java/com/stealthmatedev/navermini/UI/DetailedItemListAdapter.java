package com.stealthmatedev.navermini.UI;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdView;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class DetailedItemListAdapter extends ArrayAdapter<DetailedItem> {
    protected static class SerializableRepresentation implements Serializable {

        private final Class<? extends DetailedItemListAdapter> childClass;
        private final ArrayList<DetailedItem> results;

        private SerializableRepresentation(Class<? extends DetailedItemListAdapter> childClass, ArrayList<DetailedItem> results) {
            this.childClass = childClass;
            this.results = results;
        }
    }

    static DetailedItemListAdapter deserialize(StateManager state, DetailedItemListAdapter.SerializableRepresentation repr) {
        if (repr == null) return null;

        Class<? extends DetailedItemListAdapter> _class = repr.childClass;
        if (_class == null) return null;

        try {
            return _class.getConstructor(StateManager.class, DetailedItemListAdapter.SerializableRepresentation.class).newInstance(state, repr);
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

    protected final StateManager state;

    private final int adPosition;

    public DetailedItemListAdapter(StateManager state, ResultListQuery query, String response) {
        super(state.getActivity(), 0, new ArrayList<DetailedItem>());
        this.addAll(parseResult(response));
        this.state = state;
        int items = super.getCount();
        this.adPosition = AD_POSITION > items - 1 ? items : AD_POSITION;
    }

    public DetailedItemListAdapter(StateManager state, Serializable data) {
        super(state.getActivity(), 0, new ArrayList<DetailedItem>());
        DetailedItemListAdapter.SerializableRepresentation desData = (DetailedItemListAdapter.SerializableRepresentation) data;
        this.addAll(desData.results);
        this.state = state;
        int items = super.getCount();

        this.adPosition = AD_POSITION > items - 1 ? items : AD_POSITION;
    }

    public Serializable getDataRepresentation() {
        ArrayList<DetailedItem> results = new ArrayList<>(super.getCount());
        for (int i = 0; i <= super.getCount() - 1; i++) {
            results.add(getItem(i));
        }

        return new DetailedItemListAdapter.SerializableRepresentation(this.getClass(), results);
    }

    private View generateAd(ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_ad, parent, false);
        AdView ad = state.getActivity().resultListBannerAd();
        if (ad.getParent() != null) ((ViewGroup) ad.getParent()).removeView(ad);
        view.addView(ad);
        return view;
    }

    @Override
    public final int getCount() {
        if (super.getCount() == 0) return 0;
        else return super.getCount() + 1;
    }


    @NonNull
    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (position == adPosition) return generateAd(parent);

        if (position > adPosition) position = position - 1;

        return generateItem(position, convertView, parent);
    }

    protected boolean onItemClicked(int position) {
        return true;
    }

    protected abstract ArrayList<DetailedItem> parseResult(String result);

    protected abstract View generateItem(int position, View convertView, ViewGroup parent);

    protected abstract DetailsVisualizer getDetailsVisualizer(DetailedItem item);

}
