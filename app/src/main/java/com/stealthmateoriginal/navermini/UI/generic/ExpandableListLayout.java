package com.stealthmateoriginal.navermini.UI.generic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/09/25 0025.
 */
public class ExpandableListLayout extends ListLayout {

    private boolean collapsed = true;
    private ArrayList<Integer> heights = new ArrayList<>();
    private int totalHeight = 0;

    public ExpandableListLayout(Context context) {
        super(context);
    }

    public ExpandableListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getLayoutParams().height);
    }

    @Override
    public void populate(Adapter adapter) {
        int count = adapter.getCount();
        ViewGroup.LayoutParams thislp = getLayoutParams();
        thislp.height = 0;
        for (int i = 0; i <= count - 1; i++) {
            View v = adapter.getView(i, null, this);
            v.measure(0, 0);
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            heights.add(i, v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            totalHeight += heights.get(i);
            addView(v);
            thislp.height = totalHeight;
        }
    }

    public void toggle() {
        toggle(!this.collapsed);

    }

    public void toggle(boolean collapse) {
        this.collapsed = collapse;
        ViewGroup.LayoutParams lp = getLayoutParams();
        if(collapsed) {
            lp.height = heights.get(0);
        } else {
            lp.height = totalHeight;
        }
        invalidate();
        requestLayout();
    }

    @Override
    public void clear() {
        this.heights.clear();
        this.totalHeight = 0;
        super.clear();
    }

}
