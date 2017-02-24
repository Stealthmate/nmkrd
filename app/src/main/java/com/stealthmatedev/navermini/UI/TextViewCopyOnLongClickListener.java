package com.stealthmatedev.navermini.UI;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Stealthmate on 17/02/24 0024.
 */

public class TextViewCopyOnLongClickListener extends CopyOnLongClickListener {
    @Override
    public String getString(View v) {
        return ((TextView) v).getText().toString();
    }
}
