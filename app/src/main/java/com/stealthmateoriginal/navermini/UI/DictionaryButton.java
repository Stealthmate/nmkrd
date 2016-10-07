package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public class DictionaryButton implements IDrawerItem {

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public Object withTag(Object tag) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Object withEnabled(boolean enabled) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public Object withSetSelected(boolean selected) {
        return null;
    }

    @Override
    public boolean isSelectable() {
        return false;
    }

    @Override
    public Object withSelectable(boolean selectable) {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public View generateView(Context ctx) {
        return null;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, List payloads) {

    }

    @Override
    public boolean equals(int id) {
        return false;
    }

    @Override
    public boolean equals(long id) {
        return false;
    }

    @Override
    public Object withIdentifier(long identifier) {
        return null;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }
}
