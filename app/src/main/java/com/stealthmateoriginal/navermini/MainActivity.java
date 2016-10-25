package com.stealthmateoriginal.navermini;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.stealthmateoriginal.navermini.UI.CustomViewPager;
import com.stealthmateoriginal.navermini.UI.fragments.SearchFragment;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.StateManager;

public class MainActivity extends AppCompatActivity {

    private StateManager state;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private FrameLayout container;

    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.state = new StateManager(this);

        this.searchFragment = new SearchFragment();

        CustomViewPager pager = (CustomViewPager) findViewById(R.id.viewpager);
        pager.initialize(searchFragment, getSupportFragmentManager());
        this.pager = pager;

        this.container = (FrameLayout) findViewById(R.id.maincontainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        this.drawer = (DrawerLayout) findViewById(R.id.drawer);
        this.toggle = new ActionBarDrawerToggle(this, drawer, R.string.material_drawer_open, R.string.material_drawer_close);
        this.drawer.addDrawerListener(this.toggle);

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if(!toggle.isDrawerIndicatorEnabled()) {
            goToSearch();
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


    public void goToDetails() {
        ((CustomViewPager) findViewById(R.id.viewpager)).setCurrentItem(1);
        toggle.setDrawerIndicatorEnabled(false);
    }

    public void goToSearch() {
        ((CustomViewPager) findViewById(R.id.viewpager)).setCurrentItem(0);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        if(adapter.getCount() > 1) pager.setCurrentItem(adapter.getCount()-2);
        adapter.pop();
        System.out.println("COUNT " + adapter.getCount());
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

        //getSupportFragmentManager().beginTransaction().add(frag, null).show(frag).addToBackStack(null).commit();
        CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        adapter.push(frag);
        pager.setCurrentItem(adapter.getCount()-1);
    }
}
