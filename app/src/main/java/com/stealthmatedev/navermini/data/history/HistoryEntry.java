package com.stealthmatedev.navermini.data.history;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.stealthmatedev.navermini.data.DetailedEntry;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.kr.KrWord;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.stealthmatedev.navermini.App.APPTAG;

/**
 * Created by Stealthmate on 16/11/01 0001.
 */

public class HistoryEntry {

    private enum Type {
        KR_WORD(0, KrWord.class),
        JP_WORD(1, JpWord.class),
        JP_KANJI(2, JpKanji.class),
        EN_WORD(3, EnWord.class);

        private final int id;

        private final transient Class<? extends DetailedEntry> classType;

        Type(int id, Class<? extends DetailedEntry> classType) {
            this.id = id;
            this.classType = classType;
        }

        public static Type fromItem(DetailedEntry item) {
            Class<? extends DetailedEntry> cls = item.getClass();

            if(cls.equals(KrWord.class)) return KR_WORD;
            if(cls.equals(JpWord.class)) return JP_WORD;
            if(cls.equals(JpKanji.class)) return JP_KANJI;
            if(cls.equals(EnWord.class)) return EN_WORD;

            return null;
        }

        private static Type forId(int id) {
            switch(id){
                case 0: return KR_WORD;
                case 1: return JP_WORD;
                case 2: return JP_KANJI;
                case 3: return EN_WORD;
                default: return null;
            }
        }
    }

    public static class Deserializer implements JsonDeserializer<HistoryEntry> {

        @Override
        public HistoryEntry deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            HistoryEntry.Type type = com.stealthmatedev.navermini.data.history.HistoryEntry.Type.valueOf(obj.get("type").getAsString());
            DetailedEntry data = context.deserialize(obj.get("data"), type.classType);
            String version_id = obj.get("version_id").getAsString();
            return new HistoryEntry(version_id, type, data);
        }
    }

    public static HistoryEntry fromJson(String json) {
        return new GsonBuilder().registerTypeAdapter(HistoryEntry.class, new Deserializer()).create().fromJson(json, HistoryEntry.class);
    }

    public final String version_id;
    public final Type type;
    public final DetailedEntry data;

    public HistoryEntry() {
        this.version_id = "";
        this.type = null;
        this.data = null;
    }

    private HistoryEntry(String version_id, Type type, DetailedEntry data) {
        this.version_id =version_id;
        this.type = type;
        this.data = data;
    }

    public HistoryEntry(DetailedEntry item) {
        this.version_id = HistoryManager.VERSION;
        this.type = Type.fromItem(item);
        this.data = item;
    }

    public String getJson() {
        return new Gson().toJson(this);
    }

    public String getId() {
        return data.getRawLink();
    }

}
