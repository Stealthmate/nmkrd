package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.jp.JpWordKanjiDeserializer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import static com.stealthmatedev.navermini.Util.assertEqualJson;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class TestJpWord {

    private static final String TEST_CASE_RESULTTS_WORDONLY = "test_jp_result_wordonly.js";
    private static final String TEST_CASE_DETAILS_WORD = "test_jp_detail_word.js";
    private static final String TEST_CASE_EQUALITY = "test_jp_word_equality.js";
    private static final String TEST_CASE_INEQUALITY = "test_jp_word_inequality.js";

    private static class JpWordSerializer implements JsonSerializer<JpWord> {

        @Override
        public JsonElement serialize(JpWord src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject el = new JsonObject();
            el.addProperty("type", 0);
            el.add("obj", new Gson().toJsonTree(src));
            return el;
        }
    }

    @Test
    public void methodEqualsReturnsTrueOnEqualObject() throws IOException {
        Gson gson;
        InputStream in;
        String test;

        gson = new Gson();

        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_EQUALITY);
            test = IOUtils.toString(in, Charset.forName("utf-8"));

            JpWord[] objs = gson.fromJson(test, JpWord[].class);
            assertTrue(objs[0].equals(objs[1]));
        }
        System.out.println("Equality test succeded.");
    }

    @Test
    public void methodEqualsReturnsFalseOnDifferentObject() throws IOException {
        Gson gson;
        InputStream in;
        String test;

        gson = new Gson();

        {
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_INEQUALITY);
            test = IOUtils.toString(in, Charset.forName("utf-8"));

            JpWord[] objs = gson.fromJson(test, JpWord[].class);
            assertFalse(objs[0].equals(objs[1]));
        }
        System.out.println("Inequality test succeded.");
    }

    @Test
    public void serializationReturnsSameObject() throws IOException, ClassNotFoundException {

        Gson gson;
        InputStream in;
        String input;
        String output;

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_DETAILS_WORD);
        input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

        Entry obj = gson.fromJson(input, JpWord.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        output = Util.removeEmptyProperties(gson.toJson((JpWord)ois.readObject()), false).replaceAll("0\\.0", "0");

        assertEqualJson(input, output);

        System.out.println("Serialization test succeeded");

    }

    @Test
    public void gsonParseGeneratesSameJson() throws IOException {
        Gson gson;
        InputStream in;
        String input;
        String output;


        {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Entry.class, new JpWordKanjiDeserializer())
                    .registerTypeAdapter(JpWord.class, new JpWordSerializer())
                    .setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            in = this.getClass().getClassLoader().getResourceAsStream(TEST_CASE_RESULTTS_WORDONLY);
            input = Util.prettify(IOUtils.toString(in, Charset.forName("utf-8")));

            Entry[] objs = gson.fromJson(input, Entry[].class);
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

            Entry obj = gson.fromJson(input, JpWord.class);
            output = Util.removeEmptyProperties(gson.toJson(obj), false).replaceAll("0\\.0", "0");
            assertEqualJson(input, output);
        }

        System.out.println("Word details test succeded");
    }

}
