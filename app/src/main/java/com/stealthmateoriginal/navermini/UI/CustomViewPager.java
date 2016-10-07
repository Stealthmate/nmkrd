package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmateoriginal.navermini.state.StateManager;

import static android.R.attr.width;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class CustomViewPager extends ViewPager {

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        private static final int PAGE_SEARCH = 0;
        private static final int PAGE_DETAILS = 1;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case PAGE_SEARCH:
                    return state.getSearchFragment();
                case PAGE_DETAILS:
                    return state.getDetailsFragment();
                default:
                    return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position + 100;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private StateManager state;
    private CustomPagerAdapter adapter;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(StateManager state) {
        this.state = state;
        this.adapter = new CustomPagerAdapter(state.getActivity().getSupportFragmentManager());
        this.setAdapter(this.adapter);
    }



    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
