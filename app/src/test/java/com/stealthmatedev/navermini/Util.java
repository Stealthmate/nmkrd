package com.stealthmatedev.navermini;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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

    static String prettify(String jsonString) {
        JsonParser parser = new JsonParser();

        JsonElement json = parser.parse(jsonString);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        return gson.toJson(json);
    }


    private static String removeEmptyPropertiesObject(String json) {
        Type type = new TypeToken<Map<String, Object>[]>() {}.getType();
        Map<String, Object> data = new Gson().fromJson(json, type);

        for (Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();

            if (entry.getValue() == null) {
                it.remove();
            } else if (entry.getValue().getClass().equals(ArrayList.class)) {
                if (((ArrayList<?>) entry.getValue()).size() == 0) {
                    it.remove();
                }
            }
        }

        return new Gson().toJson(data);
    }

    private static String removeEmptyPropertiesArray(String json) {
        Type type = new TypeToken<Map<String, Object>[]>() {}.getType();
        Map<String, Object>[] data = new Gson().fromJson(json, type);


        for(int i=0;i<=data.length-1;i++) {
            for (Iterator<Map.Entry<String, Object>> it = data[i].entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Object> entry = it.next();


                if (entry.getValue() == null) {
                    it.remove();
                }
                else if(entry.getValue().getClass().equals(String.class)) {
                    if(((String) entry.getValue()).length() == 0) it.remove();
                }
                else if (entry.getValue().getClass().equals(ArrayList.class)) {
                    if (((ArrayList<?>) entry.getValue()).size() == 0) {
                        it.remove();
                    }
                }
            }
        }

        return new Gson().toJson(data);
    }


    static String removeEmptyProperties(String json, boolean isArray) {
        if(isArray) return removeEmptyPropertiesArray(json);
        else return removeEmptyPropertiesObject(json);
    }
}
