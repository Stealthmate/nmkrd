package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.stealthmateoriginal.navermini.App;
import com.stealthmateoriginal.navermini.UI.fragments.SearchFragment;

import java.util.Stack;

import static com.stealthmateoriginal.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class CustomViewPager extends ViewPager {

    public class CustomPagerAdapter extends FragmentPagerAdapter {

        private static final int PAGE_SEARCH = 0;
        private static final int PAGE_DETAILS = 1;

        private Stack<Fragment> fragStack;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragStack = new Stack<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragStack.get(position);
        }

        public void push(Fragment frag) {
            fragStack.push(frag);
            notifyDataSetChanged();
        }

        public Fragment pop() {
            if(fragStack.size() == 1) return null;
            Fragment frag = fragStack.pop();
            notifyDataSetChanged();
            return frag;
        }

        @Override
        public long getItemId(int position) {
            return fragStack.get(position).hashCode();
        }

        @Override
        public int getCount() {
            return fragStack.size();
        }

        @Override
        public int getItemPosition(Object obj) {
            if(fragStack.contains(obj)) return fragStack.indexOf(obj);
            return POSITION_NONE;
        }
    }

    private CustomPagerAdapter adapter;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(SearchFragment frag, FragmentManager fm) {
        this.adapter = new CustomPagerAdapter(fm);
        adapter.fragStack.push(frag);
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
