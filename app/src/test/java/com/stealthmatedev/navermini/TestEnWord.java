package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.en.EnWord;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;

/**
 * Created by Stealthmate on 16/10/31 0031.
 */

public class TestEnWord {

    private static final String TEST_CASE_SIMPLE = "test_en_simple.js";
    private static final String TEST_CASE_DETAILED = "test_en_detailed_fromEn.js";

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

            EnWord[] objs = gson.fromJson(input, EnWord[].class);
            output = Util.removeEmptyProperties(gson.toJson(objs), true).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }


        {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_DETAILED);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            EnWord word = gson.fromJson(input, EnWord.class);
            output = Util.removeEmptyProperties(gson.toJson(word), false);
            assertEqualJson(input, output);
        }
    }

}
