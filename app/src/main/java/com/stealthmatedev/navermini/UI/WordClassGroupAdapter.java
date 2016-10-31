package com.stealthmatedev.navermini.UI;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.WordClassGroup;
import com.stealthmatedev.navermini.data.en.EnWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public class WordClassGroupAdapter extends BaseAdapter {

    public interface OnMeaningClickedListener {
        void clicked(WordClassGroup grp, WordClassGroup.Meaning m);
    }

    private static final int TYPE_WCLASS = 0;
    private static final int TYPE_MEANING = 1;

    private final ArrayList<? extends WordClassGroup> grps;

    private final int count;
    private OnMeaningClickedListener onMeaningClickListener;

    public WordClassGroupAdapter(List<? extends WordClassGroup> wcg, OnMeaningClickedListener onMeaningClickListener) {
        grps = new ArrayList<>(wcg);
        int count = grps.size();
        for (WordClassGroup g : grps) {
            count += g.meanings.size();
        }
        this.count = count;
        this.onMeaningClickListener = onMeaningClickListener;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof WordClassGroup) {
            return TYPE_WCLASS;
        } else if (getItem(position) instanceof WordClassGroup.Meaning) {
            return TYPE_MEANING;
        }
        throw new IllegalArgumentException("Wrong item type " + getItem(position).getClass());
    }

    @Override
    public int getCount() {
        return count;
    }

    private WordClassGroup getWordClassGroup(WordClassGroup.Meaning m) {
        for (WordClassGroup g : grps) {
            if (g.meanings.contains(m)) return g;
        }

        throw new IllegalArgumentException("Invalid meaning to find group of " + m);
    }

    @Override
    public Object getItem(int position) {

        if (position == 0) return grps.get(0).wclass;

        int i = 1;
        for (WordClassGroup g : grps) {
            for (WordClassGroup.Meaning m : g.meanings) {
                if (position == i) return m;
                i++;
            }
            if (position == i) return g.wclass;
            i++;
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected View getWordClassView(WordClassGroup grp, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_detail_listitem_wclass, parent, false);
        view.setText(grp.wclass);
        return view;
    }

    protected View getMeaningView(final WordClassGroup.Meaning m, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_detail_listitem_meaning, parent, false);
        view.setText(m.m);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeaningClickListener.clicked(getWordClassGroup(m), m);
            }
        });
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_WCLASS:
                return getWordClassView((WordClassGroup) getItem(position), parent);
            case TYPE_MEANING:
                return getMeaningView((WordClassGroup.Meaning) getItem(position), parent);
            default: {
                return null;
            }
        }
    }
}
