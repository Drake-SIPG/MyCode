
/*
 * FileName：Unitl.java
 *
 * Description：杂七杂八工具类
 *
 * History：
 * 版本号 作者 日期 简介
 * 1.0 knogxiangrun 2015-09-06 Create
 */


package com.sse.sseapp.app.core.utils;


import com.fasterxml.jackson.databind.JsonNode;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    //处理空值

    /***
     * 处理空字符串
     * @param o
     * @param dv
     * @return
     */
    public static String getJNStrV(JsonNode o, String dv) {
        return o == null ? dv : o.textValue();
    }

    /***
     * 处理空字符串
     * @param o
     * @return
     */
    public static String getJNStrV(JsonNode o) {
        return getJNStrV(o, "");
    }

    public static String getObjStrV(Object o, String dv) {
        if (o instanceof JsonNode) {
            return getJNStrV((JsonNode) o, dv);
        }
        return o == null ? dv : o.toString();
    }

    public static String getObjStrV(Object o) {
        return getObjStrV(o, "");
    }


    /***
     * 处理空整形字段
     * @param o
     * @param dv
     * @return
     */
    public static int getJNIntV(JsonNode o, int dv) {
        return o == null ? dv : o.asInt();
    }

    /***
     * 处理空整形字段
     * @param o
     * @return
     */
    public static int getJNIntV(JsonNode o) {
        return getJNIntV(o, 0);
    }

    public static int getObjIntV(Object o, int dv) {
        if (o instanceof JsonNode) {
            return getJNIntV((JsonNode) o, dv);
        }
        return o == null ? dv : Integer.valueOf(o.toString().isEmpty() ? "0" : o.toString());
    }

    public static int getObjIntV(Object o) {
        return getObjIntV(o, 0);
    }


    public static Long getObjLongV(Object o, Long dv) {
        return o == null ? dv : Long.valueOf(o.toString().isEmpty() ? "0" : o.toString());
    }

    public static Long getObjLongV(Object o) {
        return getObjLongV(o, 0L);
    }

    public static double getObjDobV(Object o, double dv) {
        return o == null ? dv : Double.valueOf(o.toString().isEmpty() ? "0" : o.toString());
    }

    public static double getObjDobV(Object o) {
        return getObjDobV(o, 0);
    }


    /***
     * 处理空bool类型
     * @param o
     * @param dv
     * @return
     */
    public static boolean getJNBoolV(JsonNode o, boolean dv) {
        return o == null ? dv : (o.isBoolean() ? o.booleanValue() : "TRUE".equals(o.asText().toUpperCase()));
    }

    /***
     * 处理空bool类型
     * @param o
     * @return
     */
    public static boolean getJNBoolV(JsonNode o) {
        return getJNBoolV(o, false);
    }

    public static boolean getObjBoolV(Object o, boolean dv) {
        if (o instanceof JsonNode) {
            return getJNBoolV((JsonNode) o, dv);
        }
        return o == null ? dv : "TRUE".equals(o.toString().toUpperCase());
    }

    public static boolean getObjBoolV(Object o) {
        return getObjBoolV(o, false);
    }
    //处理空值


    /***
     * 处理List<String>
     * @param o
     * @return
     */
    public static List<String> getObjListStrV(Object o) {
        return o == null ? new ArrayList<String>() : (List<String>) o;
    }


    /***
     * 获取ip
     * @param request
     * @return
     */
    public static String getRemortIP(HttpServletRequest request) {
        String ipAddress = null;
        //ipAddress = request.getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (java.net.UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    /***
     * 获取时间字符串
     * @param format
     * @return
     */
    public static String getDate(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /***
     * 获取时间字符串
     * @param currentTime
     * @param format
     * @return
     */
    public static String getDate(Date currentTime, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 根据字符串获取日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date getDateFromString(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /***
     * 获取包含中文的长度
     * @param str
     * @return
     */
    public static int getLen(String str) {
        return str.replaceAll("[^\\x00-\\xff]", "*").length();
    }


    public static Date getDateByTimestamp(Long timestamp) {
        return new Date(timestamp * 1000);
    }

    public static Long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }


    public static String encodeBySHA1(String str) {

        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    /***
     * 仿StringUtils.join
     * @param array
     * @param separator
     * @return
     */
    public static String join(Set<String> set, String separator) {
        if (set == null) {
            return null;
        }

        Object[] array = set.toArray();

        int arraySize = array.length;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        //注意此处为正则匹配，不能用"."；
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        //取最小长度值
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    /**
     * 时间差（分钟）
     *
     * @param time1
     * @param time2
     * @param min   相差分钟数
     * @return time2-min>time1=true,time2-min<=time1=false
     */
    public static boolean DateDiffForMin(long time1, long time2, int min) {
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        calendar2.add(Calendar.MINUTE, -min);

        Date date1 = new Date(time1);
        Date date2 = calendar2.getTime();

        if (date2.compareTo(date1) > 0) {
            return true;
        }

        return false;
    }

    /**
     * 冒泡排序（字符串数字排序）
     *
     * @param sourceList
     * @return
     */
    public static void bubbleSort(List<String> sourceList) {
        for (int i = 0; i < sourceList.size() - 1; i++) { //外层循环控制排序趟数
            for (int j = 0; j < sourceList.size() - 1 - i; j++) { //内层循环控制每一趟排序多少次
                Integer tmp1 = Integer.parseInt(sourceList.get(j));
                Integer tmp2 = Integer.parseInt(sourceList.get(j + 1));

                if (tmp1 > tmp2) {
                    String temp = sourceList.get(j);
                    sourceList.set(j, sourceList.get(j + 1));
                    sourceList.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * 字符创List元素去重
     *
     * @param redStrList
     */
    public static void removeStringDuplicate(List<String> redStrList) {
        for (int i = 0; i < redStrList.size() - 1; i++) {
            for (int j = redStrList.size() - 1; j > i; j--) {
                if (redStrList.get(j).equals(redStrList.get(i))) {
                    redStrList.remove(j);
                }
            }
        }
    }

    public static int cons = 100;

    public static String getGuid() {
        cons += 1;
        long now = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(now);
        String info = now + "";
        int ran = 0;
        if (cons > 999) {
            cons = 100;
        }
        ran = cons;
        return time + info.substring(4, info.length()) + ran;
    }
}
