package com.stealthmatedev.navermini.data;

import com.google.gson.Gson;

/**
 * Created by Stealthmate on 16/11/02 0002.
 */

public class ResponseTranslator<T> {

    private final Class<? extends T> type;

    public ResponseTranslator(Class<? extends T> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return this.type;
    }

    public T translate(String response) {
        return (T) new Gson().fromJson(response, getType());
    }

}
