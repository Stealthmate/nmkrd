package com.stealthmateoriginal.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.DetailsAdapter;

/**
 * Created by Stealthmate on 16/09/22 0022.
 */
public class DetailsFragment extends Fragment {

    private ViewGroup root;

    private LinearLayout resultcontainer;

    private LinearLayout loadingView;

    public void clear() {
        if(root == null) return;
        root.removeAllViews();
        root.addView(loadingView);
        loadingView.setVisibility(View.GONE);
    }

    public void populate(DetailsAdapter adapter) {
        adapter.populateContainer(getView());
        loadingView.setVisibility(View.GONE);
    }

    public void waitForData() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.root = (ViewGroup) inflater.inflate(R.layout.layout_details, container, false);

        this.loadingView = (LinearLayout) this.root.findViewById(R.id.view_loading);
        this.loadingView.setVisibility(View.GONE);

        return this.root;
    }
}
