package com.stealthmatedev.navermini.data.jp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.stealthmatedev.navermini.data.DetailedItem;

import java.lang.reflect.Type;

/**
 * Created by Stealthmate on 16/10/31 0031.
 */

public class JpWordKanjiDeserializer implements JsonDeserializer<DetailedItem> {

    private static final String KEY_TYPE = "type";
    private static final int TYPE_DEF = 0;
    private static final int TYPE_KANJI = 1;

    @Override
    public DetailedItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject wrapper = json.getAsJsonObject();
        int id = wrapper.get("type").getAsInt();

        JsonObject obj = wrapper.get("obj").getAsJsonObject();

        if(id == TYPE_DEF) return context.deserialize(obj, JpWord.class);
        else if(id == TYPE_KANJI) return context.deserialize(obj, JpKanji.class);
        else return null;
    }
}
