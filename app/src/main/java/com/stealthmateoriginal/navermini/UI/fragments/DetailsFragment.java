package com.stealthmateoriginal.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stealthmateoriginal.navermini.MainActivity;
import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.state.StateManager;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class DetailsFragment extends Fragment {

    private ViewGroup root;

    private LinearLayout loadingView;

    private DetailsAdapter currentAdapter;

    public void clear() {
        if(root == null) return;
        root.removeAllViews();
        root.addView(loadingView);
        loadingView.setVisibility(View.GONE);
        this.currentAdapter = null;
    }

    public void populate(DetailsAdapter adapter) {
        root.addView(adapter.getView(root));
        loadingView.setVisibility(View.GONE);
        this.currentAdapter = adapter;
    }

    public void waitForData() {
        clear();
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.root = (ViewGroup) inflater.inflate(R.layout.layout_details, container, false);

        this.loadingView = (LinearLayout) this.root.findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        System.out.println("Attempt to load saved instance...");
        if(savedInstanceState != null) {
            String fragClass = savedInstanceState.getString("nm_DATA_CLASS");
            System.out.println("LOADING SAVED INSTANCE STATE");
            System.out.println(fragClass);
        }

        return this.root;
    }

    @Override
    public void onStart() {
        super.onStart();
        waitForData();
    }

    public StateManager getState() {
        return ((MainActivity)getActivity()).getState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        currentAdapter.save(outState);
    }
}
