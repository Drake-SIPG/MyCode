package com.sse.sseapp.core.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.DesensitizedUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 敏感信息脱敏处理
 */
public class SensitiveDataConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        // 获取原始日志
        String oriLogMsg = event.getFormattedMessage();

        // 获取脱敏后的日志
        return invokeMsg(oriLogMsg);
    }

    /**
     * 日志脱敏开关
     */
    private static final Boolean CONVERTER_CAN_RUN = true;

    /**
     * 日志脱敏关键字
     */
    private static final String[] DESENSITIZATION_ARRAY = {"mobile", "email", "loginName", "password", "newPwd", "cardNum"};

    /**
     * 处理日志字符串，返回脱敏后的字符串
     */
    public static String invokeMsg(final String oriMsg) {
        String tempMsg = oriMsg;
        if (CONVERTER_CAN_RUN) {
            // 处理字符串
            for (String key : DESENSITIZATION_ARRAY) {
                int index = -1;
                do {
                    index = tempMsg.indexOf(key, index + 1);
                    if (index != -1) {
                        // 判断key是否为单词字符
                        if (isWordChar(tempMsg, key, index)) {
                            continue;
                        }
                        // 寻找值的开始位置
                        int valueStart = getValueStartIndex(tempMsg, index + key.length());

                        // 查找值的结束位置（逗号，分号）
                        int valueEnd = getValueEndIndex(tempMsg, valueStart);

                        // 对获取的值进行脱敏
                        String subStr = tempMsg.substring(valueStart, valueEnd);
                        subStr = desensitization(subStr, key);
                        tempMsg = tempMsg.substring(0, valueStart) + subStr + tempMsg.substring(valueEnd);
                    }
                } while (index != -1);
            }
        }
        return tempMsg;
    }


    private static final Pattern PATTERN = Pattern.compile("[0-9a-zA-Z]");

    /**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     */
    private static boolean isWordChar(String msg, String key, int index) {
        // 必须确定key是一个单词
        if (index != 0) {
            // 判断key前面一个字符
            char preCh = msg.charAt(index - 1);
            Matcher match = PATTERN.matcher(preCh + "");
            if (match.matches()) {
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = PATTERN.matcher(nextCh + "");
        return match.matches();
    }

    /**
     * 获取value值的开始位置
     *
     * @param msg        要查找的字符串
     * @param valueStart 查找的开始位置
     */
    private static int getValueStartIndex(String msg, int valueStart) {
        // 寻找值的开始位置
        do {
            char ch = msg.charAt(valueStart);
            valueStart++;
            if (ch == ':' || ch == '=') {
                // key与 value的分隔符
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                break;
            }
        } while (true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     */
    private static int getValueEndIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);
            if (ch == '"') {
                // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',') {
                    // 去掉前面的 \  处理这种形式的数据
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                }
                break;
            } else if (ch == ';' || ch == ',' || ch == '}') {
                break;
            } else {
                valueEnd++;
            }
        } while (true);
        return valueEnd;
    }

    private static String desensitization(String str, String key) {
        switch (key) {
            case "loginName":
            case "cardNum":
                return DesensitizedUtil.idCardNum(str, 3, 2);
            case "mobile":
                return DesensitizedUtil.mobilePhone(str);
            case "email":
                return DesensitizedUtil.email(str);
            case "password":
            case "newPwd":
                return DesensitizedUtil.password(str);
            default:
                return "";
        }
    }

    public static void main(String[] args) {


        String oriLogMsg = "{\"status\":\"200\",\"msg\":\"成功\",\"data\":{\"needUpdate\":1,\"timeStamp\":\"1701067837000\",\"list\":[{\"webURL\":\"https://mobile.sse.com.cn/gwapp/gwapp2020/#/?mode=red&tag=supervise&superviseTag=superviseMeasure&version=5.1.0\"}]}}";
        String newLog = invokeMsg(oriLogMsg);
        System.out.println(oriLogMsg);
        System.out.println(newLog);
        System.out.println("==============================================");
        // 寻找值的开始位置
        int valueStart = getValueStartIndex(oriLogMsg, 105 + 6);

        System.out.println(oriLogMsg.substring(100));
        // 查找值的结束位置（逗号，分号）
        int valueEnd = getValueEndIndex(oriLogMsg, valueStart);

        // 对获取的值进行脱敏
        String subStr = oriLogMsg.substring(valueStart, valueEnd);
        System.out.println(subStr);
    }
}