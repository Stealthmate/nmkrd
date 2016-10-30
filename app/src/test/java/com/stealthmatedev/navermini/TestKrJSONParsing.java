package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.kr.KrExampleEntry;
import com.stealthmatedev.navermini.data.kr.KrWordEntry;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.google.gson.internal.UnsafeAllocator.create;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestKrJSONParsing {

    private static final String TESTCASE_KR_WORDENTRY_1 = "TESTCASE_KR_WORDENTRY_1.txt";
    private static final String TESTCASE_KR_EXAMPLEENTRY_1 = "TESTCASE_KR_EXAMPLEENTRY_1.txt";

    @Test
    public void KrWordEntryConstructionIsCorrect() throws IOException {



        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_KR_WORDENTRY_1);
        String test = IOUtils.toString(in, Charset.forName("utf-8"));

        KrWordEntry.DEBUG_TEST = true;
        KrWordEntry[] objs  = gson.fromJson(test, KrWordEntry[].class);
        assertEquals(Util.prettify(test), gson.toJson(objs));
    }

    @Test
    public void KrExampleEntryConstructionIsCorrect() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_KR_EXAMPLEENTRY_1);
        String test = IOUtils.toString(in, Charset.forName("utf-8"));

        ///Korean examples are without translation
        String[] objs  = gson.fromJson(test, String[].class);
        assertEquals(Util.prettify(test), gson.toJson(objs));
    }
}
