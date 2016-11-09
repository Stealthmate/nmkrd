package com.stealthmatedev.navermini;

import java.util.Comparator;

/**
 * Created by Stealthmate on 16/11/09 0009.
 */

public class EarliestStringMatchComparator implements Comparator<String> {

    public final String query;

    public EarliestStringMatchComparator(String query) {
        this.query = query;
    }

    @Override
    public int compare(String o1, String o2) {
        int index1 = o1.indexOf(query);
        int index2 = o2.indexOf(query);

        if (index1 == -1 && index2 > -1) return +1;
        if (index2 == -1 && index1 > -1) return -1;
        if (index1 == index2 && index1 == -1) return o1.length() - o2.length();

        if (index1 < index2) return -1;
        if (index2 < index1 ) return +1;
        return o1.length() - o2.length();
    }
}
