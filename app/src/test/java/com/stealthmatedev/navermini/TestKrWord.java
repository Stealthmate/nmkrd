package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.kr.KrWord;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;
import static com.stealthmatedev.navermini.Util.removeEmptyProperties;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestKrWord {

    private static final String TEST_CASE_SIMPLE = "test_krword_simple.js";
    private static final String TEST_CASE_DETAILED = "test_krword_detailed.js";
    private static final String TEST_CASE_EQUALITY = "test_krword_equality.js";

    @Test
    public void methodEqualsReturnsTrueOnEqualObject() throws IOException {
        Gson gson;
        InputStream in;
        String test;

        gson = new Gson();

        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_EQUALITY);
            test = IOUtils.toString(in, Charset.forName("utf-8"));

            KrWord[] objs = gson.fromJson(test, KrWord[].class);
            assertTrue(objs[0].equals(objs[1]));
        }
        System.out.println("Equality test succeded.");
    }


    @Test
    public void gsonParseGeneratesSameJson() throws IOException {

        Gson gson;
        InputStream in;
        String input;
        String output;

        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        {

            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_SIMPLE);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            KrWord[] objs = gson.fromJson(input, KrWord[].class);
            output = Util.removeEmptyProperties(gson.toJson(objs), true);
            assertEqualJson(input, output);
        }

        System.out.println("Simple test succeeded");

        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_DETAILED);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            KrWord obj = gson.fromJson(input, KrWord.class);
            output = Util.removeEmptyProperties(gson.toJson(obj), false);
            assertEqualJson(input, output);
        }

        System.out.println("Detailed test succeded");
    }
}
