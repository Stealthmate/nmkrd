package com.stealthmateoriginal.navermini.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.icu.util.Measure;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.start;

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
    private static Pattern DELIMITER = Pattern.compile("(?=[; \\(\\[\\)\\]])");

    private boolean cannotDrawAll = false;

    private ArrayList<TextToken> parseWords(String text) {
        ArrayList<TextToken> tokens = new ArrayList<>();
        String[] words = text.split(DELIMITER.pattern());
        for (int i = 0; i <= words.length - 1; i++) {
            tokens.add(new TextToken(words[i], null));
        }
        if (tokens.size() == 0) tokens.add(new TextToken(text, null));
        System.out.println(Arrays.toString(words));
        return tokens;
    }

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
                this.textTokens.addAll(parseWords(rubyless));
            }

            String lower = match.group(1);
            String upper = match.group(2);
            this.textTokens.add(new TextToken(lower, upper));

            lastIndex = end;
        }

        if (lastIndex < text.length()) {
            String rubyless = text.substring(lastIndex);
            this.textTokens.addAll(parseWords(rubyless));
        }
    }

    private boolean containsFurigana() {
        for (TextToken t : this.textTokens) {
            if (t.upper.length() > 0) return true;
        }
        return false;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        parseText();

        if (!containsFurigana()) {
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
        int padding = padding_left + padding_right;
        int width = padding + (int) text_length;

        int heightLower = (int) textPaint.getFontMetrics().bottom - (int) textPaint.getFontMetrics().top;
        int heightUpper = (int) furiganaPaint.getFontMetrics().bottom - (int) furiganaPaint.getFontMetrics().top;

        int padding_top = getPaddingTop();
        int padding_bottom = getPaddingBottom();

        int height = heightUpper + heightLower + padding_top + padding_bottom + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        int providedWidth = MeasureSpec.getSize(widthSpec);
        int providedWidthMode = MeasureSpec.getMode(widthSpec);

        if (providedWidthMode == MeasureSpec.EXACTLY && providedWidth > width)
            width = providedWidth;

        int providedHeight = MeasureSpec.getSize(heightSpec);
        int providedHeightMode = MeasureSpec.getMode(heightSpec);


        if (providedWidthMode != MeasureSpec.UNSPECIFIED && providedWidth < width) {

            System.out.println(providedWidth + " " + providedWidthMode + " PROVIDED");
            int maxLines = getMaxLines() > 0 ? getMaxLines() : Integer.MAX_VALUE;
            int currentLine = 1;
            float cursor = padding;
            int expandedHeight = height;
            for (int i = 0; i <= textTokens.size() - 1; i++) {
                TextToken t = textTokens.get(i);
                float tokenLength = Math.max(textPaint.measureText(t.lower), furiganaPaint.measureText(t.upper));
                System.out.println(tokenLength + " length");
                System.out.println("Cursor " + cursor);
                if ((cursor + tokenLength > providedWidth && currentLine == maxLines) || tokenLength > providedWidth) {
                    setMeasuredDimension(providedWidth, height * currentLine);
                    cannotDrawAll = true;
                    return;
                }
                if (cursor + tokenLength > providedWidth) {
                    currentLine++;
                    textTokens.get(i - 1).lower += "\n";
                    cursor = padding;
                    expandedHeight += height;
                    System.out.println("New line!");
                } else {
                    cursor += tokenLength;
                }
            }

            System.out.println(height + " " + expandedHeight);
            height = expandedHeight;

        } else if (providedHeightMode == MeasureSpec.EXACTLY && providedHeight > height)
            height = providedHeight;

        setMeasuredDimension(width, height);

    }

    @Override
    public void onDraw(Canvas canvas) {

        if (!containsFurigana()) {
            super.onDraw(canvas);
            return;
        }

        final float START = getPaddingLeft();

        float cursor = START;

        float furiganaMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());
        float fullLowerTextHeight = textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top;
        float fullUpperTextHeight = furiganaPaint.getFontMetrics().bottom - furiganaPaint.getFontMetrics().top;
        float fullTextHeight = fullLowerTextHeight + fullUpperTextHeight + furiganaMargin;

        float baseline = getPaddingTop() + furiganaPaint.getTextSize() - textPaint.getFontMetrics().top + furiganaMargin;
        float furiganaBaseline = baseline - textPaint.getTextSize() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        for (int i = 0; i <= textTokens.size() - 1; i++) {
            TextToken t = textTokens.get(i);
            float len_lower = textPaint.measureText(t.lower);
            float len_upper = furiganaPaint.measureText(t.upper);
            float cursor_center = cursor + (Math.abs(len_lower - len_upper) / 2);
            if (len_lower > len_upper) {
                canvas.drawText(t.lower, cursor, baseline, textPaint);
                canvas.drawText(t.upper, cursor_center, furiganaBaseline, furiganaPaint);
            } else {
                canvas.drawText(t.lower, cursor_center, baseline, textPaint);
                canvas.drawText(t.upper, cursor, furiganaBaseline, furiganaPaint);
            }

            if (t.lower.contains("\n")) {
                baseline = baseline + fullTextHeight;
                furiganaBaseline = furiganaBaseline + fullTextHeight;
                cursor = START;
            } else cursor += Math.max(len_upper, len_lower);
        }
    }

}
