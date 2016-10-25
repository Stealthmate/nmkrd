package com.stealthmateoriginal.navermini.data.kr.worddetails;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */
public class Definition implements Serializable {

    public final String def;
    public final ArrayList<String> ex;

    public Definition(String def, ArrayList<String> ex) {
        this.def = def;
        this.ex = ex;
    }

}
