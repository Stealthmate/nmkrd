package com.stealthmateoriginal.navermini.UI.jp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stealthmate on 16/09/29 0029.
 */

public class FuriganaTextView extends TextView {

    private TextPaint kanjiPaint;
    private TextPaint furiganaPaint;

    public FuriganaTextView(Context context) {
        super(context);
        this.kanjiPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
    }

    public FuriganaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.kanjiPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
    }

    public FuriganaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.kanjiPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
    }

    private static Pattern RUBY = Pattern.compile("\\(([^;\\(\\)]+);([^;\\(\\)]+)\\)");
    private static Pattern TOKEN = Pattern.compile("%[0-9]+%");

    @Override
    public void onDraw(Canvas canvas) {
        final ColorStateList csl = getTextColors();
        final int color = csl.getDefaultColor();
        final int paddingBottom = getPaddingBottom();
        final int paddingTop = getPaddingTop();
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        kanjiPaint.setTextSize(getPaint().getTextSize() * 2.0f / 3.0f);
        furiganaPaint.setTextSize(kanjiPaint.getTextSize() / 2.0f);


        float actualTextSize = kanjiPaint.getTextSize() * 2.0f/3.0f;


        String text = getText().toString();
        Matcher matcher = RUBY.matcher(text);

        ArrayList<String> kanji = new ArrayList<>();
        ArrayList<String> furigana = new ArrayList<>();

        //System.out.println("Will be drawing");
        //System.out.println(text);

        float cursor = 0.0f;

        float baseline = getBaseline();
        float furiganaBaseline = baseline + kanjiPaint.getFontMetrics().top;
        //System.out.println(baseline + " " + furiganaBaseline);

        int lastIndex = 0;

        while (matcher.find()) {
            kanji.add(matcher.group(1));
            furigana.add(matcher.group(2));

            int start = matcher.start();
            int end = matcher.end();

            String nonKanji = text.substring(lastIndex, start);
            canvas.drawText(nonKanji, cursor, baseline, kanjiPaint);
            cursor += kanjiPaint.measureText(nonKanji);

            String textKanji = kanji.get(kanji.size()-1);
            canvas.drawText(textKanji, cursor, baseline, kanjiPaint);

            String furi = furigana.get(furigana.size()-1);
            canvas.drawText(furi, 0, furi.length(), cursor, furiganaBaseline, furiganaPaint);

            cursor += kanjiPaint.measureText(textKanji);
            lastIndex = end;
        }

        canvas.drawText(text, lastIndex, text.length(), cursor, baseline, kanjiPaint);

        //System.out.println(text);

        //super.onDraw(canvas);
        //canvas.drawTextOnPath(getText().toString(), p, 0, 0, paint);
    }

}
