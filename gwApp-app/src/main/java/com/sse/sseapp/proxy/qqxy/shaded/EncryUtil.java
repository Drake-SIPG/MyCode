package com.sse.sseapp.proxy.qqxy.shaded;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kangli
 * 2018/8/18 上午10:28
 */
@SuppressWarnings("all")
public class EncryUtil<T> {
    public static final String MAIN_STARCODE = "28D408F265A9B5C3";
    private static EncryUtil instance;

    private EncryUtil() {

    }

    public static EncryUtil getInstance() {
        if (instance == null) {
            instance = new EncryUtil();
        }
        return instance;
    }

    public String EncryReqContent(String url, T t, String scode) {
        GetBaseRequest baseRequest = new GetBaseRequest();
        baseRequest.setUrl(url);
        baseRequest.setReqContent(t);
        Gson gson = new Gson();
        //加密环境
        String encrypt = null;
        try {
            encrypt = AESOperator.getInstance().encrypt(URLEncoder.encode(gson.toJson(baseRequest), "UTF-8"), scode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encrypt;

    }
}
