package com.xiaoma.moudle.webService.utils;

import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;

import java.util.HashMap;
import java.util.Map;

public class NumberUtil extends NumberUtils {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public NumberUtil() {
    }

    public static int toInt(final Object value) {
        String str = String.valueOf(value);
        return toInt(str);
    }

    public static int toInt(final String str) {
        return toInt(str, -1);
    }

    public static int toInt(@Nullable final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Integer.valueOf(str);
            } catch (NumberFormatException var3) {
                return defaultValue;
            }
        }
    }

    public static long toLong(final String str) {
        return toLong(str, 0L);
    }

    public static long toLong(@Nullable final String str, final long defaultValue) {
        if (str == null) {
            return defaultValue;
        } else {
            try {
                return Long.valueOf(str);
            } catch (NumberFormatException var4) {
                return defaultValue;
            }
        }
    }

    public static Double toDouble(String value) {
        return toDouble(value, (Double) null);
    }

    public static Double toDouble(@Nullable String value, Double defaultValue) {
        return value != null ? Double.valueOf(value.trim()) : defaultValue;
    }

    public static Float toFloat(String value) {
        return toFloat(value, (Float) null);
    }

    public static Float toFloat(@Nullable String value, Float defaultValue) {
        return value != null ? Float.valueOf(value.trim()) : defaultValue;
    }

    public static String to62String(long i) {
        int radix = DIGITS.length;
        char[] buf = new char[65];
        int charPos = 64;

        for (i = -i; i <= (long) (-radix); i /= (long) radix) {
            buf[charPos--] = DIGITS[(int) (-(i % (long) radix))];
        }

        buf[charPos] = DIGITS[(int) (-i)];
        return new String(buf, charPos, 65 - charPos);
    }

    // 将中文数字转换为整数
    public static int chineseNumberToInt(String chineseNumber) {
        // 定义中文数字与阿拉伯数字的映射
        Map<Character, Integer> digitMap = new HashMap<>();
        digitMap.put('零', 0);
        digitMap.put('一', 1);
        digitMap.put('二', 2);
        digitMap.put('三', 3);
        digitMap.put('四', 4);
        digitMap.put('五', 5);
        digitMap.put('六', 6);
        digitMap.put('七', 7);
        digitMap.put('八', 8);
        digitMap.put('九', 9);

        // 定义中文单位与倍数的映射
        Map<Character, Integer> unitMap = new HashMap<>();
        unitMap.put('十', 10);
        unitMap.put('百', 100);
        unitMap.put('千', 1000);
        unitMap.put('万', 10000);

        int result = 0;
        int temp = 0;  // 临时变量，用于处理"二十"这样的组合

        for (int i = 0; i < chineseNumber.length(); i++) {
            char c = chineseNumber.charAt(i);

            if (digitMap.containsKey(c)) {
                temp = digitMap.get(c);
            } else if (unitMap.containsKey(c)) {
                // 处理单位
                int unit = unitMap.get(c);
                if (temp == 0) {
                    // 像"十"这样的单独单位表示1*10
                    result += unit;
                } else {
                    result += temp * unit;
                }
                temp = 0;
            }
        }

        // 加上最后可能剩余的数字（如"二十三"中的"三"）
        result += temp;

        return result;
    }
}
