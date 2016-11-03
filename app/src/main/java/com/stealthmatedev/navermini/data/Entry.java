package com.stealthmatedev.navermini.data;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface Entry extends Serializable {

    class Translator extends ResponseTranslator<Entry> {
        public Translator(Class<? extends Entry> type) {
            super(type);
        }
    }

    boolean isPartial();
    String getLinkToDetails();
    String getRawLink();

}
