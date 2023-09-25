package com.damskuy.petfeedermobileapp.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static String getJsonFieldAsString(String jsonString, String fieldName) {
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        if (!jsonObject.has(fieldName)) return null;
        return jsonObject.get(fieldName).getAsString();
    }

    public static String getJsonErrorField(String jsonString) {
        return getJsonFieldAsString(jsonString, "error");
    }
}
