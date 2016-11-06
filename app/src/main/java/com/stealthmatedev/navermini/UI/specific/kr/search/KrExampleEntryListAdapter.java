package com.stealthmatedev.navermini.UI.specific.kr.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.stealthmatedev.navermini.UI.NetworkEntryListAdapter;
import com.stealthmatedev.navermini.UI.ResultListSearchVisualizer;

/**
 * Created by Stealthmate on 16/11/06 0006.
 */

public class KrExampleEntryListAdapter extends ResultListSearchVisualizer {

    public KrExampleEntryListAdapter(Context context, NetworkEntryListAdapter adapter) {
        super(context, adapter);
    }

    @NonNull
    @Override
    public View getView(@NonNull ViewGroup container){
        ListView v = (ListView) super.getView(container);
        return null;
    }

}
