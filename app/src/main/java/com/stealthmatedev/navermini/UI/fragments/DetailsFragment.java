package com.stealthmatedev.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.SentenceEntry;
import com.stealthmatedev.navermini.state.StateManager;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class DetailsFragment extends Fragment {

    private DetailsVisualizer.Observer detailsObserver;

    private ViewGroup root;

    private LinearLayout loadingView;

    private DetailsVisualizer currentAdapter;

    private boolean isCreated = false;

    public DetailsFragment() {
        detailsObserver = new DetailsVisualizer.Observer() {
            @Override
            public void onPopulated() {
                update();
            }
        };
    }

    public void clear() {
        if (root == null) return;
        root.removeAllViews();
        root.addView(loadingView);
        loadingView.setVisibility(View.GONE);
    }

    public void update() {
        if (!isCreated || this.currentAdapter == null) return;
        root.removeAllViews();
        root.addView(this.currentAdapter.getView(this, root));
        loadingView.setVisibility(View.GONE);
    }

    public void setVisualizer(DetailsVisualizer adapter) {

        if (this.currentAdapter != null) this.currentAdapter.unregisterObserver(detailsObserver);

        this.currentAdapter = adapter;

        if (this.currentAdapter == null) return;

        this.currentAdapter = adapter;
        this.currentAdapter.registerObserver(detailsObserver);
        update();
    }

    public void waitForData() {
        clear();
        Log.d(APPTAG, (root == null) + "");
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.root = (ViewGroup) inflater.inflate(R.layout.layout_details, container, false);

        return this.root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.loadingView = (LinearLayout) this.root.findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        if (this.currentAdapter == null) {
            this.currentAdapter = DetailsVisualizer.fromSavedState(savedInstanceState);
        }

        if (this.currentAdapter != null) this.currentAdapter.registerObserver(detailsObserver);

        this.isCreated = true;
        update();
    }

    public StateManager getState() {
        return ((MainActivity) getActivity()).getState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentAdapter != null) currentAdapter.saveState(outState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        if(this.currentAdapter != null) this.currentAdapter.onCreateContextMenu(this, menu, view, menuInfo);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if(this.currentAdapter != null) return this.currentAdapter.onContextItemSelected(this, item);
        return false;
    }
}
