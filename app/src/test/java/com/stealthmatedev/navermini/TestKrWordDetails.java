package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.kr.worddetails.WordDetails;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestKrWordDetails {

    private static final String TEST_CASE_1 = "TESTCASE_KR_DETAILS_1.js";
    private static final String TEST_CASE_2 = "TESTCASE_KR_DETAILS_1.js";

    private void testTestCase(String filepath) throws IOException {
        Gson gson;
        InputStream in;
        String test;

        test = Util.readFile(filepath);

        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        WordDetails obj = gson.fromJson(test, WordDetails.class);
        String output = Util.removeEmptyProperties(gson.toJson(obj), false);

        Util.assertEqualJson(test, output);
    }

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {
        testTestCase(TEST_CASE_1);
        testTestCase(TEST_CASE_2);
    }

}
