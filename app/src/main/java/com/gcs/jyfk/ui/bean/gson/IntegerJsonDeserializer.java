package com.gcs.jyfk.ui.bean.gson;

import com.gcs.jyfk.utils.MyLog;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by lyw
 * on 2016/11/22.
 */
public class IntegerJsonDeserializer implements JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsInt();
        } catch (Exception e) {
            MyLog.i("json_info", "IntegerJsonDeserializer-deserialize-error:" + (json != null ? json.toString() : ""));
            return 0;
        }
    }
}
