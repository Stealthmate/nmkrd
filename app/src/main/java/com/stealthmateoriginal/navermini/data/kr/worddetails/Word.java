package com.stealthmateoriginal.navermini.data.kr.worddetails;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class Word implements Serializable {

    public final String name;
    public final String pronunciation;
    public final String hanja;
    public final String[] classes;

    public Word(String name, String pronunciation, String hanja, String[] classes) {
        this.name = name;
        this.pronunciation = pronunciation;
        this.hanja = hanja;
        if(classes != null) this.classes = classes;
        else this.classes = new String[0];
    }
}
