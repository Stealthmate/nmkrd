package com.stealthmatedev.navermini.UI.generic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Stealthmate on 16/09/24 0024.
 */
public class UIUtils {

    public static float dp(Context context,int units) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, units, context.getResources().getDisplayMetrics());
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public static void populateViewGroup(ViewGroup container, ArrayList<View> views) {
        int count = views.size();
        for (int i = 0; i <= count - 1; i++) {
            container.addView(views.get(i));
        }
    }

    public static void populateViewGroup(ViewGroup container, Adapter adapter) {
        int count = adapter.getCount();
        for (int i = 0; i <= count - 1; i++) {
            container.addView(adapter.getView(i, null, container));
        }
    }
}