package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.stealthmatedev.navermini.R.id.result;

/**
 * Created by Stealthmate on 16/11/08 0008.
 */

public class FilterlessArrayAdapter<T> extends ArrayAdapter<T> {

    private class NoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults fr = new FilterResults();
            ArrayList<T> items = new ArrayList<>(FilterlessArrayAdapter.this.getCount());
            for (int i = 0; i < getCount(); i++) {
                items.add(getItem(i));
            }
            fr.values = items;
            fr.count = items.size();
            return fr;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }

    public FilterlessArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FilterlessArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public FilterlessArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public FilterlessArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public FilterlessArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    ;
}
