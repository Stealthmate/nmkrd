package com.stealthmateoriginal.navermini.UI.jp;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stealthmate on 16/09/29 0029.
 */

public class FuriganaTextView extends TextView {

    private static final int MARGIN_RUBY = 1;

    private class TextToken {
        String lower;
        String upper;

        TextToken(String lower, String upper) {
            this.lower = lower == null ? "" : lower;
            this.upper = upper == null ? "" : upper;
        }
    }

    private TextPaint textPaint;
    private TextPaint furiganaPaint;

    private ArrayList<TextToken> textTokens;


    public FuriganaTextView(Context context) {
        super(context);
        this.textPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
        this.textTokens = new ArrayList<>();
    }

    public FuriganaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.textPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
        this.textTokens = new ArrayList<>();
    }

    public FuriganaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.textPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
        this.textTokens = new ArrayList<>();
    }

    private static Pattern RUBY = Pattern.compile("\\(([^;\\(\\)]+);([^;\\(\\)]+)\\)");

    private void parseText() {
        this.textTokens = new ArrayList<>();

        String text = getText().toString();

        Matcher match = RUBY.matcher(text);
        int lastIndex = 0;
        while (match.find()) {
            int start = match.start();
            int end = match.end();

            if (start > lastIndex) {
                String rubyless = text.substring(lastIndex, start);
                TextToken noruby = new TextToken(rubyless, null);
                this.textTokens.add(noruby);
            }

            String lower = match.group(1);
            String upper = match.group(2);
            this.textTokens.add(new TextToken(lower, upper));
            lastIndex = end;
        }
    }

    private boolean containsFurigana() {
        for(TextToken t : this.textTokens) {
            System.out.println(t.upper);
            if(t.upper.length() > 0) return true;
        }
        return false;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        parseText();

        if(!containsFurigana()) {
            super.onMeasure(widthSpec, heightSpec);
            return;
        }

        float text_length = 0.0f;

        textPaint.setTextSize(getPaint().getTextSize());
        furiganaPaint.setTextSize(textPaint.getTextSize() / 2.0f);

        for (TextToken t : this.textTokens) {
            float len_upper = furiganaPaint.measureText(t.upper);
            float len_lower = textPaint.measureText(t.lower);
            text_length += Math.max(len_lower, len_upper);
        }

        int padding_left = getPaddingLeft();
        int padding_right = getPaddingRight();
        int width = padding_left + padding_right + (int) text_length;

        int heightLower = (int) textPaint.getFontMetrics().bottom - (int) textPaint.getFontMetrics().top;
        int heightUpper = (int) furiganaPaint.getFontMetrics().bottom - (int) furiganaPaint.getFontMetrics().top;

        int padding_top = getPaddingTop();
        int padding_bottom = getPaddingBottom();

        int height = heightUpper + heightLower + padding_top + padding_bottom + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        setMeasuredDimension(width, height);

    }

    @Override
    public void onDraw(Canvas canvas) {

        if(!containsFurigana()) {
            super.onDraw(canvas);
            return;
        }

        float cursor = 0.0f;

        float baseline = getMeasuredHeight() - getPaddingBottom() - textPaint.getFontMetrics().bottom;
        float furiganaBaseline = baseline - textPaint.getTextSize() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        for (int i = 0; i <= textTokens.size() - 1; i++) {
            TextToken t = textTokens.get(i);
            float len_lower = textPaint.measureText(t.lower);
            float len_upper = furiganaPaint.measureText(t.upper);
            float cursor_center = cursor + (Math.abs(len_lower - len_upper) / 2);
            if(len_lower > len_upper) {
                canvas.drawText(t.lower, cursor, baseline, textPaint);
                canvas.drawText(t.upper, cursor_center, furiganaBaseline, furiganaPaint);
            } else {
                canvas.drawText(t.lower, cursor_center, baseline, textPaint);
                canvas.drawText(t.upper, cursor, furiganaBaseline, furiganaPaint);
            }

            cursor += Math.max(len_upper, len_lower);
        }
    }

}
