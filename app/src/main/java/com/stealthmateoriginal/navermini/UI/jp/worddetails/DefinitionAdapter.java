package com.stealthmateoriginal.navermini.UI.jp.worddetails;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stealthmate on 16/10/03 0003.
 */

public class DefinitionAdapter extends ArrayAdapter<Definition> {

    private static final String HTML_ASSET_NAME = "jp_detail_word_meaning_webview_htmlstyle.html";

    private ArrayList<Definition> definitions;
    private String HTML;

    public DefinitionAdapter(Context context, ArrayList<Definition> definitions) {
        super(context, 0, definitions);
        this.definitions = definitions;
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(HTML_ASSET_NAME);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            this.HTML = result.toString("UTF-8");
            System.out.println(HTML);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("COULD NOT GET ASSET!");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_jp_detail_definition, parent, false);
        }

        Definition def = getItem(position);

        TextView wordclass = (TextView) convertView.findViewById(R.id.view_jp_detail_word_definition_wordclass);
        wordclass.setText(def.classname);

        ListView meanings = (ListView) convertView.findViewById(R.id.view_jp_detail_word_definition_meaninglist);
        ArrayList<String> meaningStrs = new ArrayList<>(def.meanings.length);
        for (int i = 0; i <= def.meanings.length - 1; i++) {
            meaningStrs.add(def.meanings[i].meaning);
        }
        /*meanings.setAdapter(new ArrayAdapter<String>(getContext(), 0, meaningStrs) {


            private final Pattern RUBY = Pattern.compile("\\(([^;\\(\\)]+);([^;\\(\\)]+)\\)");

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_detail_jp_definition_meaning, parent, false);
                }

                String text = getItem(position);
                Matcher match = RUBY.matcher(text);

                while(match.find()) {

                    String kanji = match.group(1);
                    String furigana = match.group(2);

                    text = text.replace(match.group(0), "<ruby>" + kanji + "<rt>" + furigana + "</rt></ruby>");
                }

                System.out.println(text);
                WebView wv = (WebView) convertView;
                wv.loadData(text, "text/html; charset=utf-8", "utf-8");
                wv.setBackgroundColor(Color.TRANSPARENT);
                System.out.println("watwatwawt");
                return convertView;
            }
        });*/
        meanings.setAdapter(new ArrayAdapter<>(getContext(), R.layout.view_detail_jp_definition_meaning, meaningStrs));

        return convertView;
    }

}
