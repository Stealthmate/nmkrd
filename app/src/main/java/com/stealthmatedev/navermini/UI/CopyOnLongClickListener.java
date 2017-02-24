package com.stealthmatedev.navermini.UI;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.stealthmatedev.navermini.R;

/**
 * Created by Stealthmate on 17/02/24 0024.
 */

public abstract class CopyOnLongClickListener implements View.OnLongClickListener {

    public abstract String getString(View v);

    @Override
    public boolean onLongClick(View v) {
        ClipboardManager cbm = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setPrimaryClip(ClipData.newPlainText(null, getString(v)));

        Toast.makeText(v.getContext(), R.string.toast_copied, Toast.LENGTH_SHORT).show();

        return true;
    }
}
