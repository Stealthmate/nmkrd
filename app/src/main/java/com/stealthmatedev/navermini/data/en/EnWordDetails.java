package com.stealthmatedev.navermini.data.en;

import com.stealthmatedev.navermini.data.en.worddetails.WordClassGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class EnWordDetails implements Serializable {

    public final String word;
    public final String extra;

    public final ArrayList<WordClassGroup> clsgrps;

    public EnWordDetails() {
        word = null;
        extra = null;
        clsgrps = null;
    }
}
