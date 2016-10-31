package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.icu.util.Measure;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import static android.R.attr.mode;

/**
 * Created by Stealthmate on 16/10/31 0031.
 */

public class AdaptableLinearLayout extends LinearLayout {

    public AdaptableLinearLayout(Context context) {
        super(context);
    }

    public AdaptableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        int maxDimension;
        int mode;
        int size;
        if (getOrientation() == VERTICAL) {
            size = MeasureSpec.getSize(heightSpec);
            mode = MeasureSpec.getMode(heightSpec);
            maxDimension = getLayoutParams().height;
        } else {
            size = MeasureSpec.getSize(widthSpec);
            mode = MeasureSpec.getMode(widthSpec);
            maxDimension = getLayoutParams().width;
        }

        if(maxDimension == 0) {
            super.onMeasure(widthSpec, heightSpec);
            return;
        }

        if (mode == MeasureSpec.UNSPECIFIED) {
            size = maxDimension;
            mode = MeasureSpec.AT_MOST;
        } else {
            if(size > maxDimension) size = maxDimension;
        }

        if(getOrientation() == VERTICAL) heightSpec = MeasureSpec.makeMeasureSpec(size, mode);
        else
            widthSpec = MeasureSpec.makeMeasureSpec(size, mode);

        super.onMeasure(widthSpec, heightSpec);
    }
}
