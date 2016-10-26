package com.stealthmateoriginal.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stealthmateoriginal.navermini.MainActivity;
import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.state.StateManager;

import static android.content.ContentValues.TAG;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class DetailsFragment extends Fragment {

    private ViewGroup root;

    private LinearLayout loadingView;

    private DetailsAdapter currentAdapter;

    private boolean isCreated = false;

    public void clear() {
        if(root == null) return;
        root.removeAllViews();
        root.addView(loadingView);
        loadingView.setVisibility(View.GONE);
        this.currentAdapter = null;
    }

    public void setCurrentAdapter(DetailsAdapter adapter) {
        this.currentAdapter = adapter;
        populate(adapter);
    }

    public void populate(DetailsAdapter adapter) {
        if(!this.isCreated) return;
        if(adapter != null) this.currentAdapter = adapter;
        if(this.currentAdapter == null) return;
        root.addView(adapter.getView(root));
        loadingView.setVisibility(View.GONE);
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

        Log.i(TAG, "ON CREATE DETAILS FRAGMENT");

        this.isCreated = true;
        populate(this.currentAdapter);

        return this.root;
    }

    public StateManager getState() {
        return ((MainActivity)getActivity()).getState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(currentAdapter != null) currentAdapter.saveState(outState);
    }
}
