package com.stealthmatedev.navermini.UI;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.state.Dictionary;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public class DictionarySpinnerAdapter extends BaseAdapter {

    private ArrayList<Dictionary> dicts;

    public DictionarySpinnerAdapter() {
        super();
        this.dicts = new ArrayList<>();
        this.dicts.add(Dictionary.KR);
        this.dicts.add(Dictionary.JP);
        this.dicts.add(Dictionary.EN);
        this.dicts.add(Dictionary.HJ);
    }

    @Override
    public int getCount() {
        return dicts.size();
    }

    @Override
    public Dictionary getItem(int position) {
        return dicts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dicts.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Dictionary dict = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_dictionary_spinner_compact, parent, false);
        }

        ImageView icon = (ImageView) convertView.findViewById(R.id.view_listitem_dictionary_spinner_icon);
        icon.setImageResource(dict.icon_large);

        return convertView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        final Dictionary dict = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_dictionary_spinner, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.view_listitem_dictionary_spinner_name);
        name.setText(dict.name);

        ImageView icon = (ImageView) convertView.findViewById(R.id.view_listitem_dictionary_spinner_icon);
        icon.setImageResource(dict.icon_large);

        return convertView;
    }
}
