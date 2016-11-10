package com.stealthmatedev.navermini.data;

import com.stealthmatedev.navermini.BuildConfig;

/**
 * Created by Stealthmate on 16/11/07 0007.
 */

public class ExampleSentence {
    public final SentenceEntry.Language from;
    public final SentenceEntry.Language to;

    public final String original;
    public final String translated;

    public final String keyword;

    public ExampleSentence(SentenceEntry.Language from, SentenceEntry.Language to, String original, String translated, String keyword) {
        this.from = from;
        this.to = to;
        this.original = original;
        this.translated = translated;
        this.keyword = keyword;
    }
}
