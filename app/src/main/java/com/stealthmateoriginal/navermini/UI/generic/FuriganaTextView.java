package com.stealthmateoriginal.navermini.UI.generic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.stealthmateoriginal.navermini.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Stealthmate on 16/09/29 0029.
 */

public class FuriganaTextView extends TextView {

    private static final float furiganaTextSizeMultiplier = 0.5f;

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

    private void setup() {
        this.textPaint = new TextPaint(getPaint());
        this.furiganaPaint = new TextPaint(getPaint());
        this.textTokens = new ArrayList<>();

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FuriganaTextView.this.parseText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public FuriganaTextView(Context context) {
        super(context);
        this.setup();
    }

    public FuriganaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    public FuriganaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setup();
    }

    private static Pattern RUBY = Pattern.compile("\\(([^;\\(\\)]+);([^;\\(\\)]+)\\)");
    private static Pattern DELIMITER = Pattern.compile("(?=[; \\(\\[])");

    private boolean cannotDrawAll = false;

    private ArrayList<TextToken> parseWords(String text) {
        ArrayList<TextToken> tokens = new ArrayList<>();
        String[] words = text.split(DELIMITER.pattern());
        for (int i = 0; i <= words.length - 1; i++) {
            tokens.add(new TextToken(words[i], null));
        }
        if (tokens.size() == 0) tokens.add(new TextToken(text, null));
        return tokens;
    }

    private void parseText() {
        parseText(this.getText().toString());
    }

    private void parseText(String text) {
        this.textTokens = new ArrayList<>();

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

        requestLayout();
    }

    private boolean containsFurigana() {
        for (TextToken t : this.textTokens) {
            if (t.upper.length() > 0) return true;
        }
        return false;
    }

    private float calculateTextLength() {
        float textLength = 0.0f;
        for (TextToken t : this.textTokens) {
            float len_upper = furiganaPaint.measureText(t.upper);
            float len_lower = textPaint.measureText(t.lower);
            textLength += Math.max(len_lower, len_upper);
        }

        return textLength;
    }

    private void initPaints() {
        textPaint = new TextPaint(getPaint());
        furiganaPaint = new TextPaint(textPaint);
        furiganaPaint.setTextSize(textPaint.getTextSize() * furiganaTextSizeMultiplier);

        textPaint.setColor(getTextColors().getDefaultColor());
        furiganaPaint.setColor(getTextColors().getDefaultColor());

    }

    private float fontHeight(Paint.FontMetrics fm) {
        return fm.bottom - fm.top;
    }

    @Override
    public void onMeasure(int widthSpec, int heightSpec) {
        if (!containsFurigana()) {
            super.onMeasure(widthSpec, heightSpec);
            return;
        }

        initPaints();

        final int providedWidth = MeasureSpec.getSize(widthSpec);
        final int providedWidthMode = MeasureSpec.getMode(widthSpec);

        final int providedHeight = MeasureSpec.getSize(heightSpec);
        final int providedHeightMode = MeasureSpec.getMode(heightSpec);

        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingBottom() + getPaddingTop();

        final int furiganaMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        float text_width = calculateTextLength();
        float text_height = (int) (fontHeight(furiganaPaint.getFontMetrics()) + fontHeight(textPaint.getFontMetrics()) + furiganaMargin);

        int calculatedWidth = horizontalPadding + (int) text_width;
        int calculatedHeight = (int) (text_height + verticalPadding + furiganaMargin);

        if(getId() == R.id.view_jp_detail_word_definition) System.out.println(calculatedWidth + " AYYY WTF " + text_width + " " + providedWidth);
        if ((providedWidthMode == MeasureSpec.EXACTLY && providedWidth > calculatedWidth) || providedWidth == 0)
            calculatedWidth = providedWidth;

        else if (providedWidthMode != MeasureSpec.UNSPECIFIED && providedWidth < calculatedWidth) {

            int maxLines = getMaxLines() > 0 ? getMaxLines() : Integer.MAX_VALUE;
            int currentLine = 1;
            float cursor = horizontalPadding;

            int expandedHeight = calculatedHeight;
            for (int i = 0; i <= textTokens.size() - 1; i++) {
                TextToken t = textTokens.get(i);

                float tokenLength = Math.max(textPaint.measureText(t.lower), furiganaPaint.measureText(t.upper));
                cursor += tokenLength;

                if ((cursor > providedWidth && currentLine == maxLines) || tokenLength > providedWidth) {
                    setMeasuredDimension(providedWidth, (int) (text_height * currentLine) + verticalPadding);
                    if(getId() == R.id.view_jp_detail_word_definition) System.out.println(cursor + " AYYY WTF " + t.lower);
                    cannotDrawAll = true;
                    return;
                }

                if (cursor > providedWidth) {
                    currentLine++;
                    textTokens.get(i - 1).lower += "\n";
                    cursor = horizontalPadding;
                    expandedHeight += text_height;
                }
            }
            calculatedHeight = expandedHeight;
            calculatedWidth = providedWidth;
        }

        if (providedHeightMode == MeasureSpec.EXACTLY && providedHeight > calculatedHeight)
            calculatedHeight = providedHeight;

        setMeasuredDimension(calculatedWidth, calculatedHeight);
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

        float fullTextHeight = fontHeight(textPaint.getFontMetrics()) + fontHeight(furiganaPaint.getFontMetrics()) + furiganaMargin;

        float baseline = getPaddingTop() + fontHeight(furiganaPaint.getFontMetrics()) - textPaint.getFontMetrics().ascent + furiganaMargin;
        float furiganaBaseline = baseline + textPaint.ascent() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_RUBY, getResources().getDisplayMetrics());

        for (int i = 0; i <= textTokens.size() - 1; i++) {
            TextToken t = textTokens.get(i);

            float len_lower = textPaint.measureText(t.lower);
            float len_upper = furiganaPaint.measureText(t.upper);

            float cursor_center = cursor + (Math.abs(len_lower - len_upper) / 2);

            float cursor_upper = cursor_center;
            float cursor_lower = cursor;

            if (len_upper > len_lower) {
                cursor_upper = cursor;
                cursor_lower = cursor_center;
            }


            canvas.drawText(t.lower, cursor_lower, baseline, textPaint);
            canvas.drawText(t.upper, cursor_upper, furiganaBaseline, furiganaPaint);

            if (t.lower.contains("\n")) {
                baseline = baseline + fullTextHeight;
                furiganaBaseline = furiganaBaseline + fullTextHeight;
                cursor = START;
            } else cursor += Math.max(len_upper, len_lower);
        }
    }

}
