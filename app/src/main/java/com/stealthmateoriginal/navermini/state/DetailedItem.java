package com.stealthmateoriginal.navermini.state;

import android.content.Context;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface DetailedItem {

    boolean hasDetails();
    String getLinkToDetails();
    DetailsAdapter createAdapterFromDetails(Context context, String details);
}
