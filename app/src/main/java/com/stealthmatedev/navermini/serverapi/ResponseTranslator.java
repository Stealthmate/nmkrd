package com.stealthmatedev.navermini.serverapi;

import com.stealthmatedev.navermini.data.Entry;

import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */
public interface ResponseTranslator {
    ArrayList<Entry> translate(String response);
}
