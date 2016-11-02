package com.stealthmatedev.navermini.data;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface DetailedItem extends Serializable {

    class Translator extends ResponseTranslator<DetailedItem> {
        public Translator(Class<? extends DetailedItem> type) {
            super(type);
        }
    }

    boolean hasDetails();
    String getLinkToDetails();

}
