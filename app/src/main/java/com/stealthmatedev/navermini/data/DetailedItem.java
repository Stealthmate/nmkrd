package com.stealthmatedev.navermini.data;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface DetailedItem extends Entry {

    class Translator extends ResponseTranslator<DetailedItem> {
        public Translator(Class<? extends DetailedItem> type) {
            super(type);
        }
    }

    boolean hasDetails();
    String getLinkToDetails();
    String getRawLink();

}
