package com.sse.sseapp.proxy.cominfo.shaded;

import com.sse.sseapp.core.utils.JsonUtil;
import com.tongweb.web.util.buf.HexUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.LoggerFactory;

/**
 * @Description: AES加解密
 */
@SuppressWarnings("all")
public class AESOperator {

    private static org.slf4j.Logger log = LoggerFactory
            .getLogger(AESOperator.class);

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        return Nested.instance;
    }

    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     */
    public String encrypt(String content, String key) {
        try {
            if (key == null) {
                return null;
            }
            if (key.length() != 16) {
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            return HexUtils.toHexString(encrypted);
        } catch (Exception e) {
            log.error("加密失败", e);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public String decrypt(String content, String key) {
        try {
            if (key == null) {
                return null;
            }
            if (key.length() != 16) {
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = HexUtils.fromHexString(content);
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original);
        } catch (Exception e) {
            log.error("解密失败", e);
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @param vector
     * @return
     * @throws Exception
     */
    public <T> T decrypt(String content, String key, String vector, Class<T> tClass) {
        try {
            if (key == null) {
                return null;
            }
            if (key.length() != 16) {
                return null;
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = HexUtils.fromHexString(content);
            byte[] original = cipher.doFinal(encrypted1);
            if (tClass.equals(String.class)) {
                return (T) new String(original);
            }
            T t = JsonUtil.parseObject(original.toString(), tClass);
            return t;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 于内部静态类只会被加载一次，故该实现方式时线程安全的！
     */
    static class Nested {
        private static AESOperator instance = new AESOperator();
    }

}
