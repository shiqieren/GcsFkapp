package com.gcs.fengkong.ui.bean.gson;

import android.util.Log;

import com.gcs.fengkong.utils.MyLog;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by lyw
 * on 2016/11/22.
 */
public class FloatJsonDeserializer implements JsonDeserializer<Float> {
    @Override
    public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsFloat();
        } catch (Exception e) {
            MyLog.i("json_info","FloatJsonDeserializer-deserialize-error:" + (json != null ? json.toString() : ""));
            return 0F;
        }
    }
}
