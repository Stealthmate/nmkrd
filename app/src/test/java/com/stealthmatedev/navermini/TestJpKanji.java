package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;
import com.stealthmatedev.navermini.data.DetailedItem;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;

/**
 * Created by Stealthmate on 16/10/31 0031.
 */

public class TestJpKanji {

    private static final String TEST_CASE_RESULT_KANJIONLY = "test_jp_result_kanjionly.js";
    private static final String TEST_CASE_DETAIL_KANJI = "test_jp_detail_kanji.js";

    private static class JpKanjiSerializer implements JsonSerializer<JpKanji> {
        @Override
        public JsonElement serialize(JpKanji src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject el = new JsonObject();
            el.addProperty("type", 1);
            el.add("obj", new Gson().toJsonTree(src));
            return el;
        }
    }

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {
        Gson gson;
        InputStream in;
        String input;
        String output;


        {
            gson = new GsonBuilder()
                    .registerTypeAdapter(DetailedItem.class, new JpWordKanjiDeserializer())
                    .registerTypeAdapter(JpKanji.class, new JpKanjiSerializer())
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_RESULT_KANJIONLY);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            DetailedItem[] objs = gson.fromJson(input, DetailedItem[].class);
            JpKanji[] words = new JpKanji[objs.length];
            for (int i = 0; i <= objs.length - 1; i++) words[i] = (JpKanji) objs[i];
            output = Util.removeEmptyProperties(gson.toJson(words), true).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }

        System.out.println("Kanji-only test succeeded");

        {
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_DETAIL_KANJI);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            DetailedItem obj = gson.fromJson(input, JpKanji.class);
            output = Util.removeEmptyProperties(gson.toJson(obj), false).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }

        System.out.println("Word details test succeded");
    }

}
