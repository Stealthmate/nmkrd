package com.stealthmatedev.navermini.state;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public enum Dictionary {
    KR(R.string.cc_kr, R.drawable.ic_kr_48dp, R.drawable.ic_kr_24dp, R.drawable.ic_kr_18dp, EntryListDictionary.KOREAN),
    JP(R.string.cc_jp, R.drawable.ic_jp_48dp, R.drawable.ic_jp_24dp, R.drawable.ic_jp_18dp, EntryListDictionary.JAPANESE),
    EN(R.string.cc_en, R.drawable.ic_en_48dp, R.drawable.ic_en_24dp, R.drawable.ic_en_18dp, EntryListDictionary.ENGLISH);

    public final int name;
    public final int icon_large;
    public final int icon_medium;
    public final int icon_small;

    public final EntryListDictionary entryListDictionary;

    Dictionary(int name, int large, int medium, int small, EntryListDictionary resdict) {
        this.name = name;
        this.icon_large = large;
        this.icon_medium = medium;
        this.icon_small = small;
        this.entryListDictionary = resdict;
    }
}
