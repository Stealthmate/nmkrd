package com.stealthmatedev.navermini.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.state.ResultListDictionary;
import com.stealthmatedev.navermini.state.ResultListQuery;
import com.stealthmatedev.navermini.state.StateManager;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import static android.content.ContentValues.TAG;

/**
 * Created by Stealthmate on 16/10/27 0027.
 */

public class ResultListSearchVisualizer extends SearchVisualizer {

    public static ResultListSearchVisualizer mapFromSearch(StateManager state, ResultListDictionary.SubDictionary dict, ResultListQuery query, String response) {

        try {
            return new ResultListSearchVisualizer(state.getActivity(), dict.resultAdapter.getConstructor(StateManager.class, ResultListQuery.class, String.class).newInstance(state, query, response));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Could not find constructor for class " + dict.resultAdapter.getName());
        return null;
    }

    private ResultListAdapter adapter;

    public ResultListSearchVisualizer(Context context, ResultListAdapter adapter) {
        super(context);
        this.adapter = adapter;
    }

    public ResultListSearchVisualizer(StateManager state, Serializable data) {
        super(state.getActivity());
        this.adapter = ResultListAdapter.deserialize(state, (ResultListAdapter.SerializableRepresentation) data);
    }

    public void setAdapter(ResultListAdapter adapter) {
        this.adapter = adapter;
    }

    public ResultListAdapter getAdapter() {
        return adapter;
    }

    @NonNull
    @Override
    public View getView(@NonNull ViewGroup container) {

        if(adapter.getCount() == 0) {
            return LayoutInflater.from(getContext()).inflate(R.layout.view_no_results, container, false);
        }

        ListView view = (ListView) LayoutInflater.from(getContext()).inflate(R.layout.layout_results, container, false);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.onItemClicked(position);
            }
        });
        return view;
    }

    @Override
    public Serializable getDataRepresentation() {
        return adapter.getDataRepresentation();
    }
}
