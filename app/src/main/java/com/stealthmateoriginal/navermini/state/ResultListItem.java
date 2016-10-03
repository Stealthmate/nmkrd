package com.stealthmateoriginal.navermini.state;

import com.stealthmateoriginal.navermini.UI.DetailsAdapter;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public interface ResultListItem {

    String getLinkToDetails();
    DetailsAdapter createAdapterFromDetails(StateManager state, String details);

}
