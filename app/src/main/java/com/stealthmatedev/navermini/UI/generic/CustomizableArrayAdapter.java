package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class CustomizableArrayAdapter<T> extends ArrayAdapter<T> {

    public interface ViewStyler {
        void style(View v);
    }

    private ViewStyler styler = null;

    public CustomizableArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomizableArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomizableArrayAdapter(Context context, int resource, T[] objects) {
        super(context, resource, objects);
    }

    public CustomizableArrayAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomizableArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
    }

    public void setViewStyler(ViewStyler styler) {
        this.styler = styler;
    }

    public ViewStyler getViewStyler() {
        return this.styler;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if(this.styler != null) this.styler.style(v);
        return v;
    }
}
