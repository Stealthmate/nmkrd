package com.stealthmateoriginal.navermini;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.stealthmateoriginal.navermini.UI.CustomViewPager;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;
import com.stealthmateoriginal.navermini.UI.fragments.SearchFragment;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.StateManager;

import static com.stealthmateoriginal.navermini.App.APPTAG;

public class MainActivity extends AppCompatActivity {

    private StateManager state;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.state = new StateManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        this.drawer = (DrawerLayout) findViewById(R.id.drawer);
        this.toggle = new ActionBarDrawerToggle(this, drawer, R.string.material_drawer_open, R.string.material_drawer_close);
        this.drawer.addDrawerListener(this.toggle);

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);


        this.searchFragment = new SearchFragment();

        final CustomViewPager pager = (CustomViewPager) findViewById(R.id.viewpager);
        pager.initialize(searchFragment, getSupportFragmentManager());
        this.pager = pager;


        DetailsAdapter adapter = DetailsAdapter.fromSavedState(this, savedInstanceState);
        if(adapter != null) {
            DetailsFragment frag = new DetailsFragment();
            frag.setCurrentAdapter(adapter);
            openNewPage(frag);
        }

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(pager.getAdapter().getCount() > 1) {

            DetailsFragment fragment = (DetailsFragment) ((FragmentPagerAdapter)pager.getAdapter()).getItem(pager.getCurrentItem());
            fragment.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(APPTAG, "CURRENT ITEM " + pager.getCurrentItem());
        this.pager.setCurrentItem(this.pager.getAdapter().getCount()-1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if(!toggle.isDrawerIndicatorEnabled()) {
            return true;
        }

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        ViewGroup root = (ViewGroup) findViewById(R.id.view_home_dict_btn_bar);
        int count = root.getChildCount();
        for (int i = 0; i <= count - 1; i++) {
            root.getChildAt(i).setBackgroundResource(0);
        }

        super.onStop();
    }

    private ViewPager pager;


    public StateManager getState() {
        return state;
    }

    @Override
    public void onBackPressed() {
        final CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        if(adapter.getCount() > 1) pager.setCurrentItem(adapter.getCount()-2);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    adapter.pop();
                    pager.removeOnPageChangeListener(this);
                }
            }
        });
    }

    public boolean onSelectDictionary(View v) {
        switch (v.getId()) {
            case R.id.view_home_dict_btn_kr: {
                state.setCurrentDictionary(ResultListDictionary.KOREAN);
            }
            break;
            case R.id.view_home_dict_btn_jp: {
                state.setCurrentDictionary(ResultListDictionary.JAPANESE);
            }
        }
        state.getSearchFragment().setSubDictionaryList(state.getCurrentDictionary());
        return true;
    }

    public void openNewPage(Fragment frag) {
        CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        adapter.push(frag);
        pager.setCurrentItem(adapter.getCount()-1);
    }
}
