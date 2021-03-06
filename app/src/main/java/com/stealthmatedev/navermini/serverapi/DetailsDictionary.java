package com.stealthmatedev.navermini.serverapi;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public enum DetailsDictionary {
    KOREAN_WORDS_DETAILS("/kr/details"),
    JAPANESE_DETAILS("/jp/details"),
    ENGLISH_WORDS_DETAILS("/en/details"),
    HANJA_DETAILS("/hj/details");

    public final String path;

    DetailsDictionary(String path) {
        this.path = path;
    }
}
