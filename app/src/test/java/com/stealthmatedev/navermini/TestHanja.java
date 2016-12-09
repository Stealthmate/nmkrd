package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.hj.HjHanja;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;

/**
 * Created by Stealthmate on 16/11/28 0028.
 */

public class TestHanja {

    private static final String TEST_CASE_SIMPLE = "test_hj_simple.js";

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {
        Gson gson;
        InputStream in;
        String input;
        String output;


        {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_SIMPLE);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            HjHanja objs = gson.fromJson(input, HjHanja.class);
            output = Util.removeEmptyProperties(gson.toJson(objs), false).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }
    }


}
