package com.stealthmatedev.navermini.UI.jp.details.word;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWord.WordClassGroup.Meaning;

import java.util.ArrayList;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/10/03 0003.
 */

public class WordclassAdapter extends ArrayAdapter<JpWord.WordClassGroup> {

    private JpWord details;

    public WordclassAdapter(Context context, JpWord details) {
        super(context, 0, new ArrayList<>(details.clsgrps));
        this.details = details;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_listitem_wclass, parent, false);
        }

        final String wordclass = getItem(position).wclass;

        TextView wordclassView = (TextView) convertView.findViewById(R.id.view_listitem_wclass_class);
        wordclassView.setText(wordclass);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_listitem_wclass_deflist);

        ArrayList<JpWord.WordClassGroup.Meaning> meaningsarr = getItem(position).meanings;
        ArrayList<String> meaningStrArr = new ArrayList<>(meaningsarr.size());
        if(meaningsarr.size() > 1) {
            for(Meaning m : meaningsarr) {
                Log.i(APPTAG, "Adding meaning " + m.m);
                if(m.m.length() > 0) meaningStrArr.add(m.m);
                else {
                    Log.i(APPTAG, "ADDING GLOSS " + m.glosses.get(0).g);
                    for(Meaning.Gloss g : m.glosses) meaningStrArr.add(g.g);
                }
            }
        } else {
            Log.i(APPTAG, "Okay wtf " + meaningsarr.get(0).glosses.size());
            for(Meaning.Gloss g : meaningsarr.get(0).glosses) {
                meaningStrArr.add(g.g);
                Log.i(APPTAG, "ADDING GLOSS " +g.g);
            }
        }

        meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_listitem_furigana, meaningStrArr));
        meanings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int meaningPosition, long id) {
                JpWordDetailsVisualizer.setDefinition(getContext(), parent.getRootView(), details.clsgrps.get(position).meanings.get(meaningPosition));
            }
        });

        return convertView;
    }

}
