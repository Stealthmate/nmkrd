package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    public static String prettify(String jsonString) {
        JsonParser parser = new JsonParser();

        JsonElement json = parser.parse(jsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.toJson(json);
    }
}
