package com.sse.sseapp.proxy.qqxy.shaded;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.Objects;
import java.util.Random;

import com.sse.sseapp.feign.system.ISysConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@SuppressWarnings("all")
@Slf4j
@Component
public class OptionSchoolScodeUtil {

    static ISysConfigFeign sysConfigFeign;

    @Autowired
    public void setSystemConfigFeign(ISysConfigFeign systemConfigFeign) {
        this.sysConfigFeign = systemConfigFeign;
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

        //TODO 参数配置 "http://option.sse.com.cn/qqxy-zt/theme/gdn";
        String url = sysConfigFeign.getConfigKey("option-scode");
        log.info("期权学苑socde请求url为:{}", url);
        String r;
        try {
            r = HttpUtil.get(url, 2000);
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

        JSONObject resultJson = JSONUtil.parseObj(r);
        String code = resultJson.getStr("code");

        if (!Objects.equals(code, "ACK")) {
            log.error("请求失败-调用期权学院接口失败  : " + r);
            return null;
        }
        String scode = resultJson.getJSONObject("data").getStr("scode");
        return PushHexUtils.getScode(scode);
    }
}
