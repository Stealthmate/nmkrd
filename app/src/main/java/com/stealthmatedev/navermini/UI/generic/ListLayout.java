package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.LinearLayout;

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

        final int orient = getOrientation();
        int spec = 0;
        if (orient == HORIZONTAL) spec = widthMeasureSpec;
        else spec = heightMeasureSpec;

        int size = MeasureSpec.getSize(spec);
        int mode = MeasureSpec.getMode(spec);

        if (mode == MeasureSpec.UNSPECIFIED) {
            size = Integer.MAX_VALUE >> 2;
            mode = MeasureSpec.AT_MOST;
            spec = MeasureSpec.makeMeasureSpec(size, mode);
        }

        if (orient == HORIZONTAL)
            super.onMeasure(spec, heightMeasureSpec);
        else super.onMeasure(widthMeasureSpec, spec);
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
