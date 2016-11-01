package com.stealthmatedev.navermini;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stealthmatedev.navermini.UI.CustomViewPager;
import com.stealthmatedev.navermini.UI.fragments.HistoryFragment;
import com.stealthmatedev.navermini.data.HistoryDB;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.util.ArrayList;

import static android.provider.Contacts.SettingsColumns.KEY;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.stealthmatedev.navermini.App.APPTAG;

public class MainActivity extends AppCompatActivity {

    private enum Page implements Serializable {
        SEEK(),
        DETAIL(),
        HISTORY(),
        TRANSITION();
    }

    private static final String KEY_FRAGMENT_COUNT = "nm_key_fragment_count";
    private static final String KEY_FRAGMENT = "nm_key_fragment_";

    private static final long EXIT_DOUBLEPRESS_TIME_THRESHOLD = 300;

    private Page currentPage = Page.SEEK;
    private long lastExitPress = Long.MAX_VALUE;


    private StateManager state;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private CustomViewPager pager;

    private AdView resultListBannerAd;

    private ArrayList<Fragment> restoreFragments(Bundle state) {

        ArrayList<Fragment> fragments = new ArrayList<>();
        if (state == null) return fragments;

        if (!state.containsKey(KEY_FRAGMENT_COUNT)) return fragments;

        int size = state.getInt(KEY_FRAGMENT_COUNT);
        fragments = new ArrayList<>(size);

        FragmentManager fm = getSupportFragmentManager();

        for (int i = 0; i <= size - 1; i++) {
            Fragment f = fm.getFragment(state, KEY_FRAGMENT + i);
            if (f == null) return fragments;
            fragments.add(f);
        }

        return fragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Fragment> storedFragments = restoreFragments(savedInstanceState);

        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() == 0) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            for (Fragment f : storedFragments) {
                trans = trans.add(f, "" + f.getId());
            }
            trans.commitNow();
        }

        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3986965759537769~5680737535");

        resultListBannerAd = new AdView(this);
        resultListBannerAd.setAdSize(AdSize.SMART_BANNER);
        resultListBannerAd.setAdUnitId("ca-app-pub-3986965759537769/6878269136");
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("47E36A7D3CA090778B4C1BF8682BF772").build();
        resultListBannerAd.loadAd(adRequest);


        this.state = new StateManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        this.drawer = (DrawerLayout) findViewById(R.id.drawer);
        this.drawer.setEnabled(false);
        this.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.toggle = new ActionBarDrawerToggle(this, drawer, R.string.material_drawer_open, R.string.material_drawer_close);
        this.drawer.addDrawerListener(this.toggle);

        //ab.setDisplayHomeAsUpEnabled(true);
        //ab.setHomeButtonEnabled(true);

        final CustomViewPager pager = (CustomViewPager) findViewById(R.id.viewpager);

        pager.initialize(getSupportFragmentManager());
        this.pager = pager;


        HistoryDB db = new HistoryDB(this);
        db.put("TEST");
        db.remove("TEST");
        db.put("hello!");
        db.put("world!");
        db.get("TEST");

        getWindow().clearFlags(FLAG_FULLSCREEN);

        Log.i(APPTAG, "DPI " + getResources().getDisplayMetrics().density);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("page", currentPage);


        FragmentManager fm = getSupportFragmentManager();

        if (fm.getFragments().size() == 0) return;

        int i = 0;
        for (Fragment f : fm.getFragments()) {
            fm.putFragment(outState, KEY_FRAGMENT + i, f);
            i++;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState == null) return;

        this.currentPage = (Page) savedInstanceState.getSerializable("page");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.pager.setCurrentItem(this.pager.getAdapter().getCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event

        if (!toggle.isDrawerIndicatorEnabled()) {
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


    public StateManager getState() {
        return state;
    }

    @Override
    public void onBackPressed() {
        final CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        //if(adapter.getCount() > 1) pager.setCurrentItem(adapter.getCount()-2);

        switch (currentPage) {
            case HISTORY: {
                pager.setCurrentItem(0);
                pager.removePages(1, -1, new CustomViewPager.Callback() {
                    @Override
                    public void callback() {
                        currentPage = Page.SEEK;
                    }
                });
            }
            break;
            case DETAIL: {
                pager.setCurrentItem(pager.getAdapter().getCount() - 2);
                pager.removePages(pager.getAdapter().getCount() - 1, -1, new CustomViewPager.Callback() {
                    @Override
                    public void callback() {
                        if (pager.getAdapter().getCount() == 1) currentPage = Page.SEEK;
                    }
                });
            }
            break;
            case SEEK: {
                long press = System.currentTimeMillis();
                if(press - lastExitPress > EXIT_DOUBLEPRESS_TIME_THRESHOLD) super.onBackPressed();
                else {
                    lastExitPress = press;
                    Toast toast = Toast.makeText(this, getString(R.string.backpress_exit_message), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            break;
        }

    }

    public void openNewDetailsPage(Fragment frag) {
        final CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        adapter.push(frag);
        pager.setCurrentItem(adapter.getCount() - 1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    currentPage = Page.DETAIL;
                }
            }
        });
    }

    private void openHistoryPage() {
        final CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        adapter.push(new HistoryFragment());
        pager.setCurrentItem(adapter.getCount() - 1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    while (adapter.getCount() > 2) adapter.remove(1);
                    pager.clearOnPageChangeListeners();
                    currentPage = Page.HISTORY;
                }
            }
        });
    }

    private void cleanup() {
        final CustomViewPager.CustomPagerAdapter adapter = (CustomViewPager.CustomPagerAdapter) pager.getAdapter();
        pager.setCurrentItem(0);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    while (adapter.getCount() > 1) adapter.remove(1);
                    pager.clearOnPageChangeListeners();
                    currentPage = Page.SEEK;
                }
            }

        });
    }

    public AdView resultListBannerAd() {
        return resultListBannerAd;
    }

    public void selectPanel(View view) {
        if (currentPage == Page.TRANSITION) return;
        switch (view.getId()) {
            case R.id.view_main_btn_searchPanel: {
                if (currentPage == Page.SEEK) return;
                currentPage = Page.TRANSITION;
                cleanup();
            }
            break;
            case R.id.view_main_btn_historyPanel: {
                if (currentPage == Page.HISTORY) return;
                currentPage = Page.TRANSITION;
                openHistoryPage();
            }
            break;
        }
    }

    public void reset() {
        if (currentPage == Page.TRANSITION) return;
        cleanup();
    }
}
