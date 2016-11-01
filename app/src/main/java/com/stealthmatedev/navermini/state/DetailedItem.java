package com.stealthmatedev.navermini.state;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface DetailedItem extends Serializable {

    boolean hasDetails();
    String getLinkToDetails();
}
