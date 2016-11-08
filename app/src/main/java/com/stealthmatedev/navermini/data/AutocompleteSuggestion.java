package com.stealthmatedev.navermini.data;

import android.widget.AutoCompleteTextView;

/**
 * Created by Stealthmate on 16/11/08 0008.
 */

public class AutocompleteSuggestion {
    public final String word;
    public final String meaning;

    public AutocompleteSuggestion(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }
}
