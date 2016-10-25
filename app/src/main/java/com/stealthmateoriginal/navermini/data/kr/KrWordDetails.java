package com.stealthmateoriginal.navermini.data.kr;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class KrWordDetails implements Serializable {
    public final KrWordEntry word;
    public final ArrayList<String> defs;


    public KrWordDetails(KrWordEntry word, ArrayList<String> defs) {
        this.word = word;
        this.defs = defs;
    }
}
