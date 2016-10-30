package com.stealthmatedev.navermini.data.kr.worddetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */
public class Definition implements Serializable {

    public final String def;
    public final ArrayList<String> ex;

    public Definition() {
        this.def = "";
        this.ex = new ArrayList<>(0);
    }

    public Definition(String def, ArrayList<String> ex) {
        this.def = def;
        if(ex != null) this.ex = ex;
        else this.ex = new ArrayList<>(0);
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Definition)) return false;

        Definition d = (Definition) obj;

        if(!def.equals(d.def)) return false;

        if (ex.size() != d.ex.size()) return false;

        for (int i = 0; i <= ex.size() - 1; i++) {
            if (!ex.get(i).equals(d.ex.get(i))) return false;
        }

        return true;
    }
}
