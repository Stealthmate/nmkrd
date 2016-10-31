package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static android.R.attr.data;
import static org.junit.Assert.assertEquals;

/**
 * Created by Stealthmate on 16/10/30 0030.
 */

public class Util {

    public static String reformatGson(String gson) {
        return gson.replaceAll("  ", "\t").replaceAll("\\n[\\t]+\\{", "{").replaceAll("([^ \\[])([:\\{])", "$1 $2").replaceAll("\\[\\n[\\t ]*([^\\n]+)\\n[ \\t]*\\],", "\\[$1\\],");
    }

    public static String reformatInputJson(String json) {
        return json;
    }

    static String readFile(String file) throws IOException {
        InputStream in = Util.class.getClassLoader().getResourceAsStream(file);
        return IOUtils.toString(in, Charset.forName("utf-8"));
    }

    static void assertEqualJson(String json1, String json2) {
        JsonParser parser = new JsonParser();
        assertEquals(parser.parse(json1), parser.parse(json2));
    }

    static String prettify(String jsonString) {
        JsonParser parser = new JsonParser();

        JsonElement json = parser.parse(jsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.toJson(json);
    }


    private static String removeEmptyPropertiesObject(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> data = new Gson().fromJson(json, type);

        cleanupMap(data);

        return new Gson().toJson(data);
    }

    private static void checkObject(Object obj, Iterator<? extends Object> it) {
        if (obj.getClass().equals(String.class)) {
            if (((String) obj).length() == 0) it.remove();
        } else if (obj.getClass().equals(ArrayList.class)) {
            if (((ArrayList<?>) obj).size() == 0) {
                it.remove();
            }
            else cleanupArray((ArrayList<Object>) obj);
        } else if (obj instanceof Map){
            cleanupMap((Map<String, Object>) obj);
        }
    }

    private static void cleanupArray(ArrayList<Object> arr) {
        for(Iterator<Object> it = arr.iterator(); it.hasNext();) {
            checkObject(it.next(), it);
        }
    }

    private static void cleanupMap(Map<String, Object> map) {
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            checkObject(it.next().getValue(), it);
        }
    }

    private static String removeEmptyPropertiesArray(String json) {
        Type type = new TypeToken<Map<String, Object>[]>() {
        }.getType();
        Map<String, Object>[] data = new Gson().fromJson(json, type);


        for (int i = 0; i <= data.length - 1; i++) {
            cleanupMap(data[i]);
        }

        return new Gson().toJson(data);
    }


    static String removeEmptyProperties(String json, boolean isArray) {
        if (isArray) return removeEmptyPropertiesArray(json);
        else return removeEmptyPropertiesObject(json);
    }
}
