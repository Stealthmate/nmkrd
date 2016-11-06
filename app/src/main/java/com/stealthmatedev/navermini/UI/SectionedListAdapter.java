package com.stealthmatedev.navermini.UI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.generic.UIUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public abstract class SectionedListAdapter<K, V> extends BaseAdapter {

    public interface OnMeaningClickedListener<K, V> {
        void clicked(K grp, V m);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SUBITEM = 1;

    private LinkedHashMap<K, ArrayList<V>> sectionMap;

    private OnMeaningClickedListener onMeaningClickListener;

    protected SectionedListAdapter(LinkedHashMap<K, ArrayList<V>> sectionMap) {
        this(sectionMap, null);
    }

    protected SectionedListAdapter(LinkedHashMap<K, ArrayList<V>> sectionMap, OnMeaningClickedListener<K, V> onMeaningClickedListener) {
        this.sectionMap = sectionMap;
        this.onMeaningClickListener = onMeaningClickedListener;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        Object obj = getItem(position);

        for (Map.Entry<K, ArrayList<V>> entry : sectionMap.entrySet()) {
            if (obj.equals(entry.getKey())) return TYPE_HEADER;
            if (entry.getValue().contains(obj)) return TYPE_SUBITEM;
        }

        return -1;
    }

    @Override
    public int getCount() {
        int count = 0;
        for (Map.Entry<K, ArrayList<V>> entry : sectionMap.entrySet()) {
            if (getHeaderText(entry.getKey()).length() > 0) count += 1;
            count += entry.getValue().size();
        }

        return count;
    }

    @Override
    public Object getItem(int position) {

        int i = 0;

        for (K key : sectionMap.keySet()) {

            if (getHeaderText(key).length() > 0) {
                if (i == position) return key;
            } else i--;
            i++;
            for (V value : sectionMap.get(key)) {
                if (i == position) return value;
                i++;
            }
        }

        return null;
    }

    protected abstract String getHeaderText(K header);

    protected abstract String getMeaningText(V meaning);

    public AdapterView.OnItemClickListener getSubitemClickListener() {
        return new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getItemViewType(position) == TYPE_HEADER) return;

                V obj = (V) getItem(position);

                K header = null;
                for (Map.Entry<K, ArrayList<V>> entry : sectionMap.entrySet()) {
                    if (entry.getValue().contains(obj)) {
                        header = entry.getKey();
                        break;
                    }
                }

                if (onMeaningClickListener != null) onMeaningClickListener.clicked(header, obj);
            }
        };
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    protected View getHeaderView(K header, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_detail_listitem_wclass, parent, false);
        String hText = getHeaderText(header);
        view.setText(hText);
        if (hText.length() == 0) {
            view.setVisibility(View.GONE);
        } else view.setVisibility(View.VISIBLE);
        return view;
    }

    protected View getMeaningView(final V m, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listitem_text_wide, parent, false);
        view.setText(getMeaningText(m));
        view.setPadding((int) (view.getPaddingLeft() + UIUtils.dp(parent.getContext(), 20)), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                return getHeaderView((K) getItem(position), parent);
            case TYPE_SUBITEM:
                return getMeaningView((V) getItem(position), parent);
            default: {
                return null;
            }
        }
    }
}
