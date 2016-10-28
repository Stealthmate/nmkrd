package com.stealthmatedev.navermini.state;

/**
 * Created by Stealthmate on 16/09/30 0030.
 */

public enum DetailsDictionary {
    KOREAN_WORDS_DETAILS("/kr/details"),
    JAPANESE_WORDS_DETAILS("/jp/details"),
    JAPANESE_KANJI_DETAILS("/jp/details/kanji");

    public final String path;

    DetailsDictionary(String path) {
        this.path = path;
    }
}
