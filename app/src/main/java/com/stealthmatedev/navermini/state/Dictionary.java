package com.stealthmatedev.navermini.state;

import com.stealthmatedev.navermini.R;
import com.stealthmatedev.navermini.serverapi.EntryListDictionary;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public enum Dictionary {
    KR(R.string.dict_name_kr, R.drawable.ic_kr_48dp, R.drawable.ic_kr_24dp, R.drawable.ic_kr_18dp, EntryListDictionary.KOREAN),
    JP(R.string.dict_name_jp, R.drawable.ic_jp_48dp, R.drawable.ic_jp_24dp, R.drawable.ic_jp_18dp, EntryListDictionary.JAPANESE),
    EN(R.string.dict_name_en, R.drawable.ic_en_48dp, R.drawable.ic_en_24dp, R.drawable.ic_en_18dp, EntryListDictionary.ENGLISH),
    HJ(R.string.dict_name_hj, R.drawable.ic_hj_48dp, R.drawable.ic_hj_24dp, R.drawable.ic_hj_18dp, EntryListDictionary.HANJA);

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
