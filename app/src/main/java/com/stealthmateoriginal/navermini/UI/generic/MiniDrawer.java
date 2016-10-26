package com.stealthmateoriginal.navermini.UI.generic;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public class MiniDrawer extends SlidingPaneLayout {

    private boolean isOpened = false;

    public MiniDrawer(Context context) {
        super(context);
    }

    public MiniDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent evt) {

        if(evt.getAction() != MotionEvent.ACTION_MOVE) return super.onInterceptTouchEvent(evt);
        float x = evt.getX();
        float y = evt.getY();

        View drawer = this.getChildAt(0);
        int[] l = new int[2];
        drawer.getLocationOnScreen(l);
        Rect rect = new Rect(l[0], l[1], l[0] + drawer.getWidth(), l[1] + drawer.getHeight());

        return super.onInterceptTouchEvent(evt);
    }

}
