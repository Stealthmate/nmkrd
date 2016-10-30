package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stealthmatedev.navermini.data.en.EnWordDetails;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class JSONParseTester {

    public static final String TESTCASE_1 = "TESTCASE_1.txt";

    private String reformatGson(String json) {
        return json.replaceAll("  ", "\t").replaceAll("\\n[\\t]+\\{", "{").replaceAll("([^ \\[])([:\\{])", "$1 $2");
    }

    @Test
    public void EnWordDetailsConstructionIsCorrect() throws JSONException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(TESTCASE_1);
        String test = IOUtils.toString(in, Charset.forName("utf-8"));

        EnWordDetails details  = gson.fromJson(test, EnWordDetails.class);
        assertEquals(test.replaceAll("\\r\\n", "\n"), reformatGson(gson.toJson(details)));
    }
}
