package com.sse.sseapp.proxy.qqxy.shaded;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Magic. Do not touch.
 *
 * @author work_pc
 * @version V1.0
 * @Title: PushHexUtils
 * @Package com.sse.push.util
 * @Description: byte[] 转hex string , hex string 转byte[]
 * @date 2018/3/29 14:39
 */
@SuppressWarnings("all")
@Component
public final class PushHexUtils {
    private static final int[] DEC = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};
    private static final byte[] HEX = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final char[] hex = "0123456789abcdef".toCharArray();

    private static String key;
    private static final String scodeOri = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String scodeVal = "2091784563ighjkzaylmbxnovwcuptdseqrfCTUDSERZFQYAPOGMNBVLHKIWJX";

    @Value("${key.aesKeyForScode}")
    public String aesKeyForScode;

    @PostConstruct
    private void setKey() {
        key = this.aesKeyForScode;
    }

    public PushHexUtils() {
    }

    public static String getScode(String scode) {
        String transScode = AESOperator.getInstance().decrypt(scode, key);

        StringBuffer transScodeBuffer = new StringBuffer();

        // 反混淆scode
        String[] transScodeElem = new String[16];
        for (int i = 0; i < 16; i++) {
            transScodeElem[i] = transScode.charAt(i) + "";
        }
        for (int i = 0; i < transScodeElem.length; i++) {
            int elemIndex = scodeOri.indexOf(transScodeElem[i]);
            transScodeBuffer.append(scodeVal.charAt(elemIndex));
        }

        return transScodeBuffer.toString();
    }

    public static int getDec(int index) {
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException var2) {
            return -1;
        }
    }

    public static byte getHex(int index) {
        return HEX[index];
    }

    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);

            for (int i = 0; i < bytes.length; ++i) {
                sb.append(hex[(bytes[i] & 240) >> 4]).append(hex[bytes[i] & 15]);
            }

            return sb.toString();
        }
    }

    public static byte[] fromHexString(String input) {
        if (input == null) {
            return null;
        } else if ((input.length() & 1) == 1) {
            throw new IllegalArgumentException("hexUtils.fromHex.oddDigits");
        } else {
            char[] inputChars = input.toCharArray();
            byte[] result = new byte[input.length() >> 1];

            for (int i = 0; i < result.length; ++i) {
                int upperNibble = getDec(inputChars[2 * i]);
                int lowerNibble = getDec(inputChars[2 * i + 1]);
                if (upperNibble < 0 || lowerNibble < 0) {
                    throw new IllegalArgumentException("hexUtils.fromHex.nonHex");
                }

                result[i] = (byte) ((upperNibble << 4) + lowerNibble);
            }

            return result;
        }
    }
}
