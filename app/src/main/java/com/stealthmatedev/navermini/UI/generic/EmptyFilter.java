package com.stealthmatedev.navermini.UI.generic;

import android.widget.Filter;

/**
 * Created by Stealthmate on 16/11/08 0008.
 */

public abstract class EmptyFilter extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        return new FilterResults();
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

    }
}
