package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/09/25 0025.
 */
public class ListLayout extends LinearLayout {


    public ListLayout(Context context) {
        super(context);
    }

    public ListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        if(mode == MeasureSpec.UNSPECIFIED) {
            size = Integer.MAX_VALUE >> 2;
            mode = MeasureSpec.AT_MOST;
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(size, mode));
    }

    public void populate(Adapter adapter) {
        int count = adapter.getCount();
        for (int i = 0; i <= count - 1; i++) {
            addView(adapter.getView(i, null, this));
        }
    }

    public void clear() {
        this.removeAllViewsInLayout();
        this.removeAllViews();
        invalidate();
    }


}
