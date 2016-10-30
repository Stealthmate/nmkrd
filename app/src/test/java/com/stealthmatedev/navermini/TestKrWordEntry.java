package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.kr.KrWordEntry;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestKrWordEntry {

    private static final String TEST_CASE_1 = "TESTCASE_KR_WORDENTRY_1.js";
    private static final String TEST_CASE_EQUALITY = "TESTCASE_KR_WORDENTRY_EQUALITY.js";

    @Test
    public void methodEqualsReturnsTrueOnEqualObject() throws IOException {
        Gson gson;
        InputStream in;
        String test;

        gson = new Gson();

        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_EQUALITY);
            test = IOUtils.toString(in, Charset.forName("utf-8"));

            KrWordEntry[] objs  = gson.fromJson(test, KrWordEntry[].class);

            assertTrue(objs[0].equals(objs[1]));
        }


    }

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {

        Gson gson;
        InputStream in;
        String test;

        //App.DEBUG_TEST = true;

        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        //Test case 1
        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_1);
            test = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            KrWordEntry[] objs  = gson.fromJson(test, KrWordEntry[].class);
            String output = Util.prettify(Util.removeEmptyProperties(gson.toJson(objs), true));

            assertEquals(test, output);
        }

        //Test case 2
        {


        }
    }
}
