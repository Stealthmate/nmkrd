package com.stealthmateoriginal.navermini.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.stealthmateoriginal.navermini.R;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/26 0026.
 */

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);

        ArrayList<String> strs = new ArrayList<>();
        strs.add("Hello");
        strs.add("World!");

        ListView list = (ListView) root.findViewById(R.id.view_history_list);
        list.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_minimal, strs));

        return root;
    }

}
