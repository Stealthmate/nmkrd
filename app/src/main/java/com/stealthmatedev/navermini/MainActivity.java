package com.stealthmatedev.navermini;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.stealthmatedev.navermini.UI.fragments.DetailsFragment;
import com.stealthmatedev.navermini.UI.fragments.HistoryFragment;
import com.stealthmatedev.navermini.UI.fragments.SearchFragment;
import com.stealthmatedev.navermini.UI.fragments.SentenceStoreFragment;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.sentencestore.SentenceStoreManager;
import com.stealthmatedev.navermini.state.StateManager;

import java.util.Stack;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_PAGE_COUNT = "nm_key_fragment_count";
    private static final String KEY_PAGE = "nm_key_fragment_";

    private static class Page {
        private final Fragment fragment;
        private final String tag;

        Page(String tag, Fragment fragment) {
            this.tag = tag;
            this.fragment = fragment;
        }
    }

    private static final String FTAG_SEARCH = "nm_FTAG_SEARCH";
    private static final String FTAG_HISTORY = "nm_FTAG_HISTORY";
    private static final String FTAG_DETAILS = "nm_FTAG_DETAILS";
    private static final String FTAG_SENTENCE_STORE = "nm_FTAG_SENTENCE_STORE";

    private Stack<Page> pagestack;

    private static final long EXIT_DOUBLEPRESS_TIME_THRESHOLD = 300;

    private long lastExitPress = 0;

    private FrameLayout content;

    private StateManager state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.state = new StateManager(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        this.content = (FrameLayout) findViewById(R.id.main_content);

        this.pagestack = new Stack<>();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_PAGE_COUNT)) {
                int count = savedInstanceState.getInt(KEY_PAGE_COUNT);
                for (int i = 0; i < count; i++) {
                    String tag = savedInstanceState.getString(KEY_PAGE + i);
                    Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
                    pagestack.push(new Page(tag, f));
                }
            }
        } else {
            Page search = new Page(FTAG_SEARCH, new SearchFragment());
            this.pagestack.push(search);
            getSupportFragmentManager().beginTransaction().add(this.content.getId(), search.fragment, search.tag).commitNow();
        }

        updateBackButton();

        getWindow().clearFlags(FLAG_FULLSCREEN);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void updateBackButton() {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(pagestack.size() > 1);
    }

    private FragmentTransaction beginTransaction() {
        return getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out);
    }

    private Page openNewPage(Fragment fragment, String tag) {
        for (Page p : pagestack) {
            if (p.tag.equals(tag)) {
                pagestack.remove(p);
                Page old = pagestack.peek();
                pagestack.push(p);
                beginTransaction().hide(old.fragment).show(p.fragment).commitNow();
                return p;
            }
        }

        Page p = new Page(tag, fragment);
        Page old = pagestack.peek();
        pagestack.push(p);
        beginTransaction().hide(old.fragment).add(this.content.getId(), p.fragment, p.tag).commitNow();
        updateBackButton();
        return p;
    }

    public DetailsFragment openNewDetailsPage(DetailedEntry entry) {
        String tag = FTAG_DETAILS + entry.getLinkToDetails();
        return (DetailsFragment) openNewPage(new DetailsFragment(), tag).fragment;
    }

    private void navigateBack() {
        Page p = pagestack.pop();
        Page pbehind = pagestack.peek();
        beginTransaction().remove(p.fragment).show(pbehind.fragment).commitNow();
        updateBackButton();
    }

    private void navigateHome() {
        FragmentTransaction ft = beginTransaction();
        while (pagestack.size() > 1) {
            Page p = pagestack.pop();
            ft = ft.hide(p.fragment).remove(p.fragment);
        }
        ft = ft.show(pagestack.peek().fragment);
        ft.commitNow();
        updateBackButton();
    }

    public void onBurgerBack() {
        if (pagestack.size() > 1) onBackPressed();
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
            case android.R.id.home: {
                onBurgerBack();
            }
            break;
            case R.id.menu_clear_history: {
                state.history().clearHistory();
                Toast.makeText(this, R.string.toast_cleared_history, Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.menu_history: {
                Page page = null;
                for (Page p : pagestack) {
                    if (p.tag.equals(FTAG_HISTORY)) {
                        page = p;
                        break;
                    }
                }
                if (page != null) {
                    onBackPressed();
                    break;
                }
                openNewPage(new HistoryFragment(), FTAG_HISTORY);
            }
            break;
            case R.id.menu_sentence_store: {
                Page page = null;
                for (Page p : pagestack) {
                    if (p.tag.equals(FTAG_SENTENCE_STORE)) {
                        page = p;
                        break;
                    }
                }
                if (page != null) {
                    onBackPressed();
                    break;
                }
                openNewPage(new SentenceStoreFragment(), FTAG_SENTENCE_STORE);
            }
            break;
            case R.id.menu_clear_sentence_store: {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle(R.string.label_menu_clear_sentence_store);
                builder.setMessage(R.string.dialog_clear_sentance_message);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        state.sentenceStore().removeAll(new SentenceStoreManager.Callback() {
                            @Override
                            public void callback(Object result) {
                                Toast.makeText(MainActivity.this, R.string.toast_cleared_sentence_store, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton(android.R.string.cancel, null);
                builder.create().show();
            }
            break;
            case R.id.menu_home: {
                navigateHome();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_PAGE_COUNT, pagestack.size());
        for (int i = 0; i < pagestack.size(); i++) {
            outState.putString(KEY_PAGE + i, pagestack.get(i).tag);
        }
    }

    public StateManager getState() {
        return state;
    }

    @Override
    public void onBackPressed() {

        if (pagestack.size() > 1) {
            navigateBack();
            return;
        }

        long press = System.currentTimeMillis();
        if (press - lastExitPress <= EXIT_DOUBLEPRESS_TIME_THRESHOLD) super.onBackPressed();
        else {
            lastExitPress = press;
            Toast toast = Toast.makeText(this, getString(R.string.toast_backpress_exit), Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
