package com.sse.sseapp.jpush.push.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface PushModel {

    public static Gson GSON = new Gson();
    public JsonElement toJSON();
    
}
