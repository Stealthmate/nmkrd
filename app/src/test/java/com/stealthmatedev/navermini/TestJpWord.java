package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;
import com.stealthmatedev.navermini.data.kr.KrWord;
import com.stealthmatedev.navermini.state.DetailedItem;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestJpWord {

    private static final String TEST_CASE_RESULTTS_WORDONLY = "test_jp_result_wordonly.js";
    private static final String TEST_CASE_DETAILS_WORD = "test_jp_detail_word.js";
    private static final String TEST_CASE_RESULTS_MIXED = "test_jp_result_mixed.js";
    private static final String TEST_CASE_EQUALITY = "test_jpword_equality.js";

    private static class JpWordSerializer implements JsonSerializer<JpWord> {

        @Override
        public JsonElement serialize(JpWord src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject el = new JsonObject();
            el.addProperty("type", 0);
            el.add("obj", new Gson().toJsonTree(src));
            return el;
        }
    }

    /*@Test
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
    }*/

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {
        Gson gson;
        InputStream in;
        String input;
        String output;


        {
            gson = new GsonBuilder()
                    .registerTypeAdapter(DetailedItem.class, new JpWordKanjiDeserializer())
                    .registerTypeAdapter(JpWord.class, new JpWordSerializer())
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_RESULTTS_WORDONLY);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            DetailedItem[] objs = gson.fromJson(input, DetailedItem[].class);
            JpWord[] words = new JpWord[objs.length];
            for (int i = 0; i <= objs.length - 1; i++) words[i] = (JpWord) objs[i];
            output = Util.removeEmptyProperties(gson.toJson(words), true).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }

        System.out.println("Word-only test succeeded");

        {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_DETAILS_WORD);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            DetailedItem obj = gson.fromJson(input, JpWord.class);
            output = Util.removeEmptyProperties(gson.toJson(obj), false).replaceAll("0\\.0", "0");
            System.out.println(output);
            assertEqualJson(input, output);
        }

        System.out.println("Word details test succeded");
    }

}
