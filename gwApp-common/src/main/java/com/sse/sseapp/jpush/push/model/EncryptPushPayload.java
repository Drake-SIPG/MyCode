package com.sse.sseapp.jpush.push.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sse.sseapp.jpush.push.model.audience.Audience;

public class EncryptPushPayload {

    private static Gson _gson = new GsonBuilder().disableHtmlEscaping().create();

    private String payload;

    private Audience audience;

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return _gson.toJson(this);
    }

}
