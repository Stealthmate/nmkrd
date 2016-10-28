package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.stealthmatedev.navermini.UI.fragments.SearchFragment;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class CustomViewPager extends ViewPager {

    public class CustomPagerAdapter extends PagerAdapter {

        class Page {
            private Fragment frag;
            private boolean alive;

            Page(Fragment frag, boolean alive) {
                this.frag = frag;
                this.alive = alive;
            }
        }

        private ArrayList<Page> pagelist;
        private FragmentManager fm;

        private CustomPagerAdapter(FragmentManager fm) {
            super();
            this.fm = fm;
            this.pagelist = new ArrayList<>();
            if (fm.getFragments() != null) {
                for(Fragment frag : fm.getFragments()) {
                    this.pagelist.add(new Page(frag, false));
                }
            } else {
                push(new SearchFragment());
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Page page = pagelist.get(position);
            if(!page.frag.isAdded()) {
                Log.wtf(APPTAG, "Fragment not added? " + page.frag.toString());
            }

            container.addView(pagelist.get(position).frag.getView());
            container.invalidate();
            page.alive = true;

            return pagelist.get(position);
        }

        private void destroyPage(Page page) {
            pagelist.remove(page);
            fm.beginTransaction().remove(page.frag).commitNow();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object obj) {

            Page page = (Page) obj;

            container.removeView(page.frag.getView());
            container.invalidate();

            if(page.alive) page.alive = false;
            else destroyPage(page);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((Page) object).frag.getView();
        }

        public void remove(int i) {
            if (pagelist.size() == 1 || i < 1 || i >= pagelist.size()) return;

            Page page = pagelist.remove(i);
            page.alive = false;
            notifyDataSetChanged();
        }

        public void push(Fragment frag) {
            pagelist.add(new Page(frag, false));
            fm.beginTransaction().add(frag, frag.getId() + "").commitNow();
            notifyDataSetChanged();
        }

        public void pop() {
            remove(pagelist.size() - 1);
        }

        @Override
        public int getCount() {
            return pagelist.size();
        }

        @Override
        public int getItemPosition(Object obj) {
            if (pagelist.contains(obj)) return pagelist.indexOf(obj);
            return POSITION_NONE;
        }
    }

    private CustomPagerAdapter adapter;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initialize(FragmentManager fm) {
        this.adapter = new CustomPagerAdapter(fm);
        this.setAdapter(this.adapter);
    }

    public interface Callback {
        void callback();
    }

    public void removePages(int pstart, int pend, final Callback cb) {

        if(pstart == -1) pstart = 0;
        if(pend == -1) pend = adapter.getCount();

        if(pstart >= adapter.getCount()) return;
        if(pend < 1 || pend < pstart) return;

        final int start = pstart;
        final int end = pend;

        this.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    for(int i=start;i<end;i++) adapter.remove(i);
                    CustomViewPager.this.removeOnPageChangeListener(this);
                    cb.callback();
                }
            }
        });
    }


/*
    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
*/
}
