package com.sse.sseapp.app.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.Random;

/**
 * sm4 加解密工具类
 *
 * @author mateng
 * @since 2023/3/23
 */
public class Sm4Util {

    private static final Mode MODE = Mode.CBC;
    private static final Padding PADDING = Padding.PKCS5Padding;
    private static final String ALGORITHM = "SM4/CBC/PKCS5Padding";
    private static final byte[] IV = new byte[16];
    private static final String CHARSET = "UTF-8";
    private static final int DEFAULT_KEY_SIZE = 128;
    private static final String SCODE_ORI = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * sm4解密
     *
     * @param content   Base64位加密后密文
     * @param keyBase64 Base64位加密后key
     * @return 解密后明文
     */
    public static String decrypt(String content, String keyBase64) {
        byte[] key = Base64.decode(keyBase64);
        SM4 sm4 = new SM4(MODE, PADDING, key, IV);
        return StrUtil.str(sm4.decrypt(content), CHARSET);
    }

    /**
     * 生成加密key
     *
     * @return Base64加密后key
     */
    public static String generateKey() {
        StringBuilder keyBuffer = new StringBuilder();

        Random random = new Random();
        String scodeOriTmp = SCODE_ORI;
        // 获取16为scode值
        for (int i = 0; i < 16; i++) {
            int charIndex = random.nextInt(scodeOriTmp.length());
            String matchChar = scodeOriTmp.charAt(charIndex) + "";
            keyBuffer.append(matchChar);
            scodeOriTmp = scodeOriTmp.replace(matchChar, "");
        }

        return Base64.encode(keyBuffer);
    }

    /**
     * sm4加密
     *
     * @param content   需加密明文
     * @param keyBase64 Base64加密后key
     * @return Base64加密后密文
     */
    public static String encrypt(String content, String keyBase64) {
        byte[] key = Base64.decode(keyBase64);
        SymmetricCrypto sm4 = new SM4(MODE, PADDING, key, IV);
        return Base64.encode(sm4.encrypt(content));
    }

    public static void main(String[] args) {
        //        String content = "{\"base\": {\"deviceId\": \"test_d50162f1bfab\",\"OSType\": \"test_262fcc8be697\",\"deviceType\": \"test_6f02dca70a6b\",\"appVersion\": \"test_3c73dc3701d2\",\"timestamp\": \"test_5474a7aed677\",\"appBundle\": \"test_2b5536984a28\",\"ip\": \"test_e5846631a79b\",\"vendor\": \"test_3c02246a07f8\",\"accessToken\": \"test_f75d5f8d0929\"},\"reqContent\": {},\"route\": \"test_187dabd1c47e\"}";
        //        String key = generateKey();
        //
        //        System.out.println("Base64Key -----  " + key);
        //        System.out.println("key -----  " + StrUtil.str(Base64.decode(key),CHARSET));
        //        String res =  encrypt(content, key);
        //        System.out.println("密文 -----  " + res);
        //        //String key = "oHUIy94zWD3nsahm";
        //        //String res = "lyeDyQdIPgqbHEozP1yZH/v0S5Tnpz7YcnQ5oop1E4AKP8D6q2rM/hLUODJZ4ftEruSTxMqC8DbKC/vNyPt18JfPkFn3iXm9KwJGbRoZq0uxuE2xiCmi6cOg0O0hxKzLgm15hkxFpHO3dCltOenpP+F3UD23S3v8713ZOO7zrsi1QSVX7YtW9Zulmsvb8wuyNTMfbVitoen+disjaUfRevl6ui1a7q5pVHjYCkSQ7QXtQFshrR3LJYHtIkGNNvNcGEd9bABZtDIP0Yw9eqJ/a+6b/z6raOxJ+9roSLNqeQ3eUFzfk7vcUOMq3flRoRXiaqvl4BODwp5R8lIcB2vrpOIGc4ll8C75Wn28SHphdDONTuIMfhE1aSY1WWG63GqGfB9o49140i8Iy6IMoiWCFFDPHIWKS/Pe0RQ0iJApskFDczSHgfcvjDFmEwXc5vT004wEZWdljcO20UIsIdI0Xw==";
        //        String output = decrypt(res, key);
        //        System.out.println("解密后明文  ----   " + output);

        String data = decrypt("wHHDHP+Jg4j4F9EhkUoTc88G3EqoV8FQD1YbU8ry6TglJVJG/ZWkCpy9ng9Lwi1p9GnfAc62AihIpznQtbv8Mx7sCEurGBeveV93lV80euA=", "WFBkVTZoSEVqbkJJZTJwNA==");
        //System.out.println(data);

        String encrypt = encrypt("{\"reqContent\":{\"loginName\":\"18317053485\",\"password\":\"Aaa111111\"}}", "eWR0WFBzOG1DcmtIV1M3aQ==");
        //System.out.println(encrypt);

        String result = decrypt("W5iXhZei+AIOIg5Qb7nSJryG52ke1VNIbIkiC3v4yyBrPbsiaQJMDbAfyTXF4CWXLFjiADavPNxikFsWQgR86onyRgH3J/g9FiR0GT5YbzqrFAWid37zG/MN1hatXqRhvxQcyjW0pOZdKP+jKT0EfubqeOP0VM16OwamBkJE0SSNXb0JDCePNRNhcMSfxx6SPEy6cLfz+rM/kS2ipQcUmM16+4YE5Lxfjn8z8B4LQPgYm6CIYuAcBBNip75dRCVgl8GCqUfbpLlE6qCo8p93LKUc3nPpcccf7+mTvNZ53lJc3k7OE31LWddSexVtl+FvnYV3qOdQ/mmIzD9EDBZeToqsFxZgAGPc/XsM70I37GNyliCFoNIDk77mX22gkxBuj6TLLdV9Tyso1fbzkEkJn9iF5SrvQdNtW3/kD6b/RdPmZ7NJJ0ZFSQIxZApVq1pASoVig9Q11yzXDHFDhF9oim2FBYQ54FXtMEpeXXlURwQ3Dy1AX5W/0k9J4/EjUm5Ve0Wwh9z4sPAJw/LwA6rf5s6S7mXpyXlYIdZlPDl93qKDqsCn0pf42Ky/T616gozulUlXcpC1vUeRJW8mvnbKZdiU5+hyC0BULVl3lSbpvLIqQBVeBrCZRC+GzolvMEwTHPgO0PzjEiPcpufmO6T0NSrTYvcbS8O0oxRxnl2bJfed5cnnl1jf5emj8kTvWRHAbocjbuDAViMjeawhOnhJ82L+ZjJHvtw52NHFIp0lVkw+PD+WZHS+Wo4FrZe2hr7L2WKsQFngd51Sg3tAJviW/q/22sHd0ovWbzgkx7/7piHuWsjAHEp3NHbbdubx9YY5pZkChVi/Kw8432mk1D4R/KkBuYCDotLdS/wrLaNbDwZoi/PT5F8uiXbeYUnNDAEsnswmp5NgrhalG81go4jgqN/6gisKSO8q8bq8rdYnpn37eKHhmf8E8sQ6Vo8eMMjENgFzAzqWHhOsMym6AtV4i5YgR1D6qnbEXn4OEKS/JeptkOc8ypcjNhtaRcuoYEuAwgOTuctKxka7ZO0GAQihfAdoOh/2CCl3+8KF2UdsFwUxFZaTjoAqDamcsGzmUOtPTPANb08qsVbG2U77rdcClRy1bET5Qz8wfDl50xQXJcQG0/5w5lt1QGQSi9W6y7hKwKIXqJujPdyb5e4y6yHZvg3JdZpeYjO+skqp4VKkWQ94H/Xz5d225U/er1MQusV+BwDe66O7hl45WGeEyiVWW9Qa2Xz+uo+dTsg+vQKl7Dv1HGecbLkpOJdpQkodHAMSV9+qU22yvM3Dq4zXtwkAhGP8e6wkoPmynDqRAygoKttg28QghA0mi+R5P2hITaf2eyi/TujKN2ysCPCiUtGS7gGFCzEKhRYywzeO5CEuU0oHM8533YAgZzfbaICrgixcn8evL1huadRn7Aq+PhUt6i2jM4MnomWhqByPGMexZ0jWdToXVCVrnsDPijdxHoJkn4da2DVHCfVDF5M6KzZtiQ==", "eWR0WFBzOG1DcmtIV1M3aQ==");
        //System.out.println(result);
    }
}
