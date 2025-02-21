package com.sse.sseapp.proxy.cominfo.shaded;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sse.sseapp.core.utils.JsonUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sse.sseapp.feign.system.ISysConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@SuppressWarnings("all")
@Slf4j
@Component
public class ScodeUtil {

    static ISysConfigFeign sysConfigFeign;

    @Autowired
    public void setSystemConfigFeign(ISysConfigFeign systemConfigFeign) {
        this.sysConfigFeign = systemConfigFeign;
    }

    public static String key;

    @Value("${key.appkey}")
    public String appkey;

    @PostConstruct
    private void setKey() {
        key = this.appkey;
    }

    //对比加密文
    private static final String messageKeyOri = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String messageKeyVal = "2091784563ighjkzaylmbxnovwcuptdseqrfCTUDSERZFQYAPOGMNBVLHKIWJX";

    /**
     * 生成scode（AES加解密key）
     */
    public static String generateScode() {
        StringBuffer scodeBuffer = new StringBuffer();

        Random random = new Random();
        String scodeOriTmp = messageKeyOri;
        // 获取16为scode值
        for (int i = 0; i < 16; i++) {
            int charIndex = random.nextInt(scodeOriTmp.length());
            String matchChar = scodeOriTmp.charAt(charIndex) + "";
            scodeBuffer.append(matchChar);
            scodeOriTmp = scodeOriTmp.replace(matchChar, "");
        }

        return scodeBuffer.toString();
    }

    /**
     * 混淆scode（AES加解密key）
     */
    public static String transScode(String scode) {
        StringBuffer transScodeBuffer = new StringBuffer();

        // 混淆scode
        String[] scodeElem = new String[16];
        for (int i = 0; i < 16; i++) {
            scodeElem[i] = scode.charAt(i) + "";
        }
        for (int i = 0; i < scodeElem.length; i++) {
            int elemIndex = messageKeyVal.indexOf(scodeElem[i]);
            transScodeBuffer.append(messageKeyOri.charAt(elemIndex));
        }

        return transScodeBuffer.toString();
    }

    public static String getScode(String transScode) {
        StringBuffer transScodeBuffer = new StringBuffer();

        // 反混淆scode
        String[] transScodeElem = new String[16];
        for (int i = 0; i < 16; i++) {
            transScodeElem[i] = transScode.charAt(i) + "";
        }
        for (int i = 0; i < transScodeElem.length; i++) {
            int elemIndex = messageKeyOri.indexOf(transScodeElem[i]);
            transScodeBuffer.append(messageKeyVal.charAt(elemIndex));
        }

        return transScodeBuffer.toString();
    }

    @SuppressWarnings("all")
    public static String getScode() {
        String resultVal;

        //参数 map
        JSONObject jsonBase = new JSONObject();
        jsonBase.put("appBundle", "com.sse.ssegwapp");

        JSONObject jsonData = new JSONObject();
        jsonData.put("base", jsonBase);

        Map<String, Object> mapReq = new HashMap<>();
        mapReq.put("data", jsonData);

        // TODO 修改url为配置获取
        String url = sysConfigFeign.getConfigKey("comInfo-scode");
        String r;
        try {
            r = HttpUtil.post(url, mapReq, 2000);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("请求失败-调用框架接口失败  : " + e.getMessage());
            //            return AppReturnCode.OtherError.getValue();
            return null;

            /*getDeviceInfo().setAccessToken("");
            getDeviceInfo().setUID("");
            setBaseInfo();

            setFail(AppReturnCode.OtherError);
			renderJsonp(getResultMap());
			return "";*/

        }

        //        JSONObject resultJson = JSONObject.parseObject(r);
        //        String rstatus = resultJson.get("state") + "";
        //        String rmsg = resultJson.get("msg") + "";
        //        String rdata = resultJson.get("data") + "";

        // 修改
        ObjectNode objectNode = JsonUtil.toObjectNode(r);
        String rstatus = objectNode.get("state").asText();
        String rmsg = objectNode.get("msg").asText();
        String rdata = objectNode.get("data").asText() ;
        //验证失败
        if (!"1".equals(rstatus)) {
            log.error("请求失败 " + rstatus + "..." + rmsg);
            return r;

        	/*getDeviceInfo().setAccessToken("");
            getDeviceInfo().setUID("");
            setBaseInfo();
        	setFail(AppReturnCode.DecryptError.getValue(), rmsg);
			renderJsonp(getResultMap());
			return "";*/
        }
        return decodeScode(rdata);
    }

    public static String getScodeXC() {
        String resultVal;

        //参数 map
        JSONObject jsonBase = new JSONObject();
        jsonBase.put("appBundle", "com.sse.ssegwapp");

        JSONObject jsonData = new JSONObject();
        jsonData.put("base", jsonBase);

        Map<String, Object> mapReq = new HashMap<>();
        mapReq.put("data", jsonData);

        // TODO 修改url为配置获取
        String url = sysConfigFeign.getConfigKey("comInfo-scode-xc");
        String r;
        try {
            r = HttpUtil.post(url, mapReq, 2000);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("请求失败-调用框架接口失败  : " + e.getMessage());
            //            return AppReturnCode.OtherError.getValue();
            return null;

            /*getDeviceInfo().setAccessToken("");
            getDeviceInfo().setUID("");
            setBaseInfo();

            setFail(AppReturnCode.OtherError);
			renderJsonp(getResultMap());
			return "";*/

        }

        //        JSONObject resultJson = JSONObject.parseObject(r);
        //        String rstatus = resultJson.get("state") + "";
        //        String rmsg = resultJson.get("msg") + "";
        //        String rdata = resultJson.get("data") + "";

        // 修改
        ObjectNode objectNode = JsonUtil.toObjectNode(r);
        String rstatus = objectNode.get("state").asText();
        String rmsg = objectNode.get("msg").asText();
        String rdata = objectNode.get("data").asText() ;
        //验证失败
        if (!"1".equals(rstatus)) {
            log.error("请求失败 " + rstatus + "..." + rmsg);
            return r;

        	/*getDeviceInfo().setAccessToken("");
            getDeviceInfo().setUID("");
            setBaseInfo();
        	setFail(AppReturnCode.DecryptError.getValue(), rmsg);
			renderJsonp(getResultMap());
			return "";*/
        }
        return decodeScode(rdata);
    }

    public static String decodeScode(String scode) {

        //得到结果解密
        String resultVal = AESOperator.getInstance().decrypt(scode, key);
        if (resultVal == null) {
            log.error("*** === 解密失败 ");
            //            return AppReturnCode.DecryptError.getValue();
            return null;

            //        	getDeviceInfo().setAccessToken("");
            //            getDeviceInfo().setUID("");
            //            setBaseInfo();
            //        	setFail(rstatus, "解密失败");
            //			renderJsonp(getResultMap());
            //			return "";
        }

        return ScodeUtil.getScode(resultVal.substring(0, 16));
    }

    /**
     * 返回string
     *
     * @param jsonObject
     * @return
     */
    public static String encryptJson(String json) {
        String urlStr = null;
        try {
            urlStr = URLEncoder.encode(json, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlStr;

    }
}
