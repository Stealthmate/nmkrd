package com.stealthmatedev.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stealthmatedev.navermini.MainActivity;
import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.state.StateManager;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class DetailsFragment extends Fragment {

    private ViewGroup root;

    private LinearLayout loadingView;

    private DetailsVisualizer currentAdapter;

    private boolean isCreated = false;

    public void clear() {
        if (root == null) return;
        root.removeAllViews();
        root.addView(loadingView);
        loadingView.setVisibility(View.GONE);
        this.currentAdapter = null;
    }

    public void setCurrentAdapter(DetailsVisualizer adapter) {
        this.currentAdapter = adapter;
        populate(adapter);
    }

    public void populate(DetailsVisualizer adapter) {
        if (adapter != null) this.currentAdapter = adapter;
        if (this.currentAdapter == null) return;
        if (!this.isCreated) return;
        root.addView(this.currentAdapter.getView(root));
        loadingView.setVisibility(View.GONE);
    }

    public void waitForData() {
        clear();
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

        if(this.currentAdapter == null) this.currentAdapter = DetailsVisualizer.fromSavedState(savedInstanceState);

        this.isCreated = true;
        populate(this.currentAdapter);
    }

    public StateManager getState() {
        return ((MainActivity) getActivity()).getState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (currentAdapter != null) currentAdapter.saveState(outState);
    }
}
