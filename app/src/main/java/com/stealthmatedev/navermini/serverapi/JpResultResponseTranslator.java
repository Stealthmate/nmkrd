package com.stealthmatedev.navermini.serverapi;

import com.stealthmatedev.navermini.data.DetailedItem;
import com.stealthmatedev.navermini.data.ResponseTranslator;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class JpResultResponseTranslator extends ResponseTranslator<DetailedItem> {
    public JpResultResponseTranslator(Class<? extends DetailedItem> type) {
        super(type);
    }
}
