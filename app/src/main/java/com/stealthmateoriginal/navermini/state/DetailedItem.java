package com.stealthmateoriginal.navermini.state;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;
import com.stealthmateoriginal.navermini.UI.fragments.DetailsFragment;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface DetailedItem {

    boolean hasDetails();
    String getLinkToDetails();
    DetailsAdapter createAdapterFromDetails(DetailsFragment fragment, String details);
}
