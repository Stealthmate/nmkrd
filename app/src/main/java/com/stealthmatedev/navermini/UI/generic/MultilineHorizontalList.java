package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Stealthmate on 16/12/16 0016.
 */

public class HorizontalListView extends RecyclerView {
    public HorizontalListView(Context context) {
        super(context);
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public HorizontalListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public HorizontalListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }


}
