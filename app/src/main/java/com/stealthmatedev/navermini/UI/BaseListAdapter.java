package com.stealthmatedev.navermini.UI;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.google.android.gms.ads.AdView;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public abstract class BaseListAdapter extends BaseAdapter implements ListAdapter {
    protected static class SerializableRepresentation implements Serializable {

        protected final Class<? extends BaseListAdapter> childClass;

        protected SerializableRepresentation(Class<? extends BaseListAdapter> childClass) {
            this.childClass = childClass;
        }
    }

    static BaseListAdapter deserialize(StateManager state, BaseListAdapter.SerializableRepresentation repr) {
        if (repr == null) return null;

        Class<? extends BaseListAdapter> _class = repr.childClass;
        if (_class == null) return null;

        try {
            return _class.getConstructor(StateManager.class, BaseListAdapter.SerializableRepresentation.class).newInstance(state, repr);
        } catch (NoSuchMethodException e) {
            Log.e(APPTAG, "Cannot instantiate DetailedItemListAdapter: Constructor from SerializableRepresentation not declared in " + _class.getSimpleName());
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

    public BaseListAdapter() {
    }

    public Serializable getDataRepresentation() {
        return new BaseListAdapter.SerializableRepresentation(this.getClass());
    }

    private View generateAd(ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_ad, parent, false);
        AdView ad = StateManager.getState(parent.getContext()).getActivity().resultListBannerAd();
        if (ad.getParent() != null) ((ViewGroup) ad.getParent()).removeView(ad);
        view.addView(ad);
        return view;
    }

    @Override
    public final int getCount() {
        if (getItemCountInternal() == 0) return 0;
        else return getItemCountInternal() + 1;
    }

    @NonNull
    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(position == 0) return generateAd(parent);
        return generateItem(position-1, convertView, parent);
    }

    public final boolean onItemClicked(int position) {
        if (position == 0) return true;
        return onClick(position-1);
    }

    protected abstract int getItemCountInternal();

    protected abstract View generateItem(int position, View convertView, ViewGroup parent);

    protected abstract boolean onClick(int position);

    @Override
    public final boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public final boolean isEnabled(int position) {
        return true;
    }


}
