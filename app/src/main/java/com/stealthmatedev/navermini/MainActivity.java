package com.stealthmatedev.navermini;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.fragments.HistoryFragment;
import com.stealthmatedev.navermini.UI.fragments.SearchFragment;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.ArrayList;
import java.util.Stack;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.stealthmatedev.navermini.App.ADUNIT;
import static com.stealthmatedev.navermini.App.APPTAG;
import static com.stealthmatedev.navermini.App.MY_PUB_ID;
import static com.stealthmatedev.navermini.App.TEST_DEVICES;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_FRAGMENT_COUNT = "nm_key_fragment_count";
    private static final String KEY_FRAGMENT = "nm_key_fragment_";

    private static class Page {
        private final Fragment fragment;
        private final String tag;

        private boolean shown;

        Page(String tag, Fragment fragment) {
            this.tag = tag;
            this.fragment = fragment;
            this.shown = false;
        }
    }

    private static final String FTAG_SEARCH = "nm_FTAG_SEARCH";
    private static final String FTAG_HISTORY = "nm_FTAG_HISTORY";
    private static final String FTAG_DETAILS = "nm_FTAG_DETAILS";

    private Stack<Page> pagestack;

    public static final int PAGE_SEARCH = 0;
    public static final int PAGE_HISTORY = 1;

    private static final long EXIT_DOUBLEPRESS_TIME_THRESHOLD = 300;

    private long lastExitPress = 0;

    private FrameLayout content;

    private StateManager state;

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

        MobileAds.initialize(this, MY_PUB_ID);

        resultListBannerAd = new AdView(this);
        resultListBannerAd.setAdSize(AdSize.SMART_BANNER);
        resultListBannerAd.setAdUnitId(ADUNIT);

        AdRequest.Builder adBuilder = new AdRequest.Builder();
        for (int i = 0; i <= TEST_DEVICES.length - 1; i++) {
            adBuilder = adBuilder.addTestDevice(TEST_DEVICES[i]);
        }
        AdRequest adRequest = adBuilder.build();

        resultListBannerAd.loadAd(adRequest);

        LinearLayout adContainer = (LinearLayout) findViewById(R.id.view_ad_container);
        adContainer.addView(resultListBannerAd);

        this.state = new StateManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        this.content = (FrameLayout) findViewById(R.id.main_content);

        this.pagestack = new Stack<>();
        Page search = new Page(FTAG_SEARCH, new SearchFragment());
        this.pagestack.push(search);
        getSupportFragmentManager().beginTransaction().add(this.content.getId(), search.fragment, search.tag).commitNow();

        this.getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            private int lastCount = 0;

            @Override
            public void onBackStackChanged() {
                int count = getSupportFragmentManager().getBackStackEntryCount();

                if(count < lastCount) {
                    pagestack.pop();
                }
                lastCount = count;
            }
        });

        getWindow().clearFlags(FLAG_FULLSCREEN);
    }

    private FragmentTransaction getBaseTransaction() {
        return getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
    }

    private FragmentTransaction transactionHideAllPages() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = getBaseTransaction();
        Fragment f = fm.findFragmentByTag(pagestack.peek().tag);
        trans = trans.hide(f);
        return trans;
    }

    private void openNewPage(Fragment fragment, String tag) {
        FragmentTransaction trans = transactionHideAllPages();
        Page p = new Page(tag, fragment);
        pagestack.push(p);
        trans.add(this.content.getId(), fragment, tag).addToBackStack(null).commit();
    }

    private void showPage(String tag) {

        if(pagestack.peek().tag.equals(tag)) return;

        FragmentTransaction trans = transactionHideAllPages();
        for (Page p : pagestack) {
            if (tag.equals(p.tag)) {
                pagestack.remove(p);
                pagestack.push(p);
                trans.show(p.fragment).commit();
                return;
            }
        }
    }

    public void openNewDetailsPage(Fragment frag) {
        int i = 0;
        for(Page p : pagestack){
            if(p.fragment instanceof DetailsFragment) i++;
        }
        String tag = FTAG_DETAILS + i;
        openNewPage(frag, tag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_appbar_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings: {
                state.history().clearHistory();
                Toast.makeText(this, "Cleared history", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.menu_history: {
                Fragment f = getSupportFragmentManager().findFragmentByTag(FTAG_HISTORY);
                if(f != null) showPage(FTAG_HISTORY);
                else openNewPage(new HistoryFragment(), FTAG_HISTORY);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public StateManager getState() {
        return state;
    }

    @Override
    public void onBackPressed() {

        if(pagestack.size() > 1) {
            super.onBackPressed();
            return;
        }

        long press = System.currentTimeMillis();
        if (press - lastExitPress <= EXIT_DOUBLEPRESS_TIME_THRESHOLD) super.onBackPressed();
        else {
            lastExitPress = press;
            Toast toast = Toast.makeText(this, getString(R.string.backpress_exit_message), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    public AdView resultListBannerAd() {
        return resultListBannerAd;
    }
}
