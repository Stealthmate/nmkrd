package com.stealthmateoriginal.navermini.UI.generic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Stealthmate on 16/09/20 0020.
 */
public class FixedListView extends ListView {

    public FixedListView(Context context) {
        super(context);
    }

    public FixedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newsize = Integer.MAX_VALUE >> 2;
        int newmode = MeasureSpec.AT_MOST;


        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(newsize, newmode));
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}