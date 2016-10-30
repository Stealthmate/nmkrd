package com.stealthmatedev.navermini.data.kr.worddetails;

import java.io.Serializable;

/**
 * Created by Stealthmate on 16/10/22 0022.
 */

public class Word implements Serializable {

    public final String word;
    public final String hanja;
    public final String pronun;
    public final String wclass;

    public Word() {
        this.word = null;
        this.pronun = null;
        this.hanja = null;
        this.wclass = null;
    }

    public Word(String word, String pronun, String hanja, String wclass) {
        this.word = word;
        this.pronun = pronun;
        this.hanja = hanja;
        this.wclass = wclass;
    }
}
