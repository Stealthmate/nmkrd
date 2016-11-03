package com.stealthmatedev.navermini.serverapi;

import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.ResponseTranslator;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class JpResultResponseTranslator extends ResponseTranslator<Entry> {
    public JpResultResponseTranslator(Class<? extends Entry> type) {
        super(type);
    }
}
