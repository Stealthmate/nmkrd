package com.stealthmateoriginal.navermini.UI;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.stealthmateoriginal.navermini.R;
import com.stealthmateoriginal.navermini.UI.jp.JpWordsAdapter;
import com.stealthmateoriginal.navermini.UI.kr.KrWordsAdapter;
import com.stealthmateoriginal.navermini.state.ResultListDictionary;
import com.stealthmateoriginal.navermini.state.ResultListItem;
import com.stealthmateoriginal.navermini.state.SearchEngine;
import com.stealthmateoriginal.navermini.state.StateManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/09/23 0023.
 */
public abstract class ResultListAdapter extends ArrayAdapter<ResultListItem> {

    private static final int PAGE_SIZE = 10;

    protected final StateManager state;
    protected final String query;
    private int page;

    private boolean noMoreAvailable;
    private boolean loading;

    public static ResultListAdapter mapFromSearch(StateManager state, ResultListDictionary.SubDictionary dict, String query, String response) {

        try {
            return dict.resultAdapter.getConstructor(StateManager.class, String.class, String.class).newInstance(state, query, response);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println("REIGNING CHAOS!");
        return null;
    }

    public ResultListAdapter(StateManager state, String query, String response) {
        super(state.getActivity(), 0, new ArrayList<ResultListItem>());
        this.addAll(parseResult(response));
        this.noMoreAvailable = super.getCount() == 0;
        this.state = state;
        this.query = query;
        this.page = 1;
        this.loading = false;
    }

    protected abstract ArrayList<ResultListItem> parseResult(String result);

    protected abstract View generateItem(int position, View convertView, ViewGroup parent);

    @NonNull
    @Override
    public final View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if(position == getCount() - 1 && !noMoreAvailable) {
            if(loading) {
                View v =LayoutInflater.from(getContext()).inflate(R.layout.view_loading, parent, false);
                v.setVisibility(View.VISIBLE);
                return v;
            }
            else return LayoutInflater.from(getContext()).inflate(R.layout.view_result_final, parent, false);
        }
        return generateItem(position, convertView, parent);
    }

    @Override
    public final int getCount() {

        if(noMoreAvailable) return super.getCount();
        else return super.getCount() + 1;
    }

    private void setNoMoreAvailable(boolean b) {

        this.noMoreAvailable = b;
        notifyDataSetChanged();
    }

    public final boolean onItemClicked(View view, int position, long id) {
        if(position == getCount() - 1) {
            if(!noMoreAvailable) loadMoreIfAvailable();
        }
        else {

            state.loadDetails(getItem(position));
            return true;
        }

        return true;
    }

    private void setLoading(boolean b) {
        this.loading = b;
        this.notifyDataSetChanged();
    }

    private void loadMoreIfAvailable() {
        state.query(query, page +1, PAGE_SIZE, new SearchEngine.OnResponse() {
            @Override
            public void responseReady(String response) {
                setLoading(false);
                ArrayList<ResultListItem> new_entries = parseResult(response);
                if(new_entries.size() < PAGE_SIZE ) {
                    setNoMoreAvailable(true);
                }
                ResultListAdapter.this.addAll(new_entries);
                page++;
            }
        });
        setLoading(true);
    }
}
