package com.stealthmateoriginal.navermini.state;

import com.stealthmateoriginal.navermini.R;

import javax.xml.transform.Result;

/**
 * Created by Stealthmate on 16/10/06 0006.
 */

public enum Dictionary {
    KR(R.string.dict_name_kr, R.drawable.ic_kr_48dp, R.drawable.ic_kr_24dp, R.drawable.ic_kr_18dp, ResultListDictionary.KOREAN),
    JP(R.string.dict_name_jp, R.drawable.ic_jp_48dp, R.drawable.ic_jp_24dp, R.drawable.ic_jp_18dp, ResultListDictionary.JAPANESE),
    EN(R.string.dict_name_en, R.drawable.ic_en_48dp, R.drawable.ic_en_24dp, R.drawable.ic_en_18dp, ResultListDictionary.JAPANESE);

    public final int name;
    public final int icon_large;
    public final int icon_medium;
    public final int icon_small;

    public final ResultListDictionary resultListDictionary;

    Dictionary(int name, int large, int medium, int small, ResultListDictionary resdict) {
        this.name = name;
        this.icon_large = large;
        this.icon_medium = medium;
        this.icon_small = small;
        this.resultListDictionary = resdict;
    }
}
