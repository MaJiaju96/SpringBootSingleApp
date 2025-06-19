package com.xiaoma.moudle.webService.utils;

import cn.hutool.core.date.DateException;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeComputationUtil {
    /**
     * 如果计算时间超过当前日期则会返回当前日期
     *
     * @param TimeUnit   Calendar类的时间单位,例如Calendar.SECOND
     * @param dateString String时间字符串
     * @return string
     * @throws ParseException
     */
    public static String timeComputatorNotInFuture(int TimeUnit, String dateString, int duration, String datePattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        String sf;
        Date current = new Date();
        if (datePattern.contains("yyyyMMdd")) {
            format = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = format.parse(dateString);
            Calendar c = Calendar.getInstance();
            c.setTime(parse);
            c.add(TimeUnit, duration);
            if (c.getTime().after(current)) {
                sf = format.format(current).replace("-", "");
            } else {
                sf = format.format(c.getTime()).replace("-", "");
            }
            return sf;
        }
        Date date = format.parse(dateString);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(TimeUnit, duration);
        if (c.getTime().after(current)) {
            sf = format.format(current);
        } else {
            sf = format.format(c.getTime());
        }
        return sf;
    }

    /**
     * 计算时间
     *
     * @param timeUnit   Calendar类的时间单位,例如Calendar.SECOND
     * @param dateString
     * @return string
     * @throws ParseException
     */
    public static String timeComputator(int timeUnit, String dateString, int duration) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sf = "";
        try {
            Calendar c = Calendar.getInstance();
            Date parse = format.parse(dateString);
            c.setTime(parse);
            c.add(timeUnit, duration);
            sf = format.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf;
    }

    /**
     * 计算时间
     *
     * @param timeUnit Calendar类的时间单位,例如Calendar.SECOND
     * @param date
     * @return string
     * @throws ParseException
     */
    public static String timeComputator(int timeUnit, Date date, int duration) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sf = "";
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(timeUnit, duration);
            sf = format.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf;
    }

    /**
     * 计算时间
     *
     * @param timeUnit Calendar类的时间单位,例如Calendar.SECOND
     * @param date
     * @return date
     * @throws ParseException
     */
    public static Date dateComputator(int timeUnit, Date date, int duration) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(timeUnit, duration);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算时间
     *
     * @param timeUnit   Calendar类的时间单位,例如Calendar.SECOND
     * @param dateString
     * @return date
     * @throws ParseException
     */
    public static Date dateComputator(int timeUnit, String dateString, int duration) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(dateString));
            c.add(timeUnit, duration);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * date 的 pattern修改
     *
     * @param date        Date日期类
     * @param datePattern String正则
     * @return String
     */
    public static String timePatternTransformer(Date date, String datePattern) {
        SimpleDateFormat format = new SimpleDateFormat(datePattern);
        String sf = format.format(date.getTime());
        return sf;
    }

    public static String timePatternTransformer(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sf = format.format(date.getTime());
        return sf;
    }

    public static String timePatternTransformer(String dateString) {
        SimpleDateFormat format;
        String sf = "";
        if (StringUtils.isEmpty(dateString)) {
            return sf;
        }
        try {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = format.parse(dateString);
            sf = format.format(parse);
            return sf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf;
    }

    /**
     * date 的 pattern修改
     *
     * @param dateString  String时间字符串
     * @param datePattern String正则
     * @return String
     */
    public static String timePatternTransformer(String dateString, @NotNull String datePattern) {
        SimpleDateFormat format;
        String sf = "";
        if (StringUtils.isBlank(dateString)) {
            return sf;
        }
        try {
            if (datePattern.contains("yyyyMMdd")) {
                format = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = format.parse(dateString);
                sf = format.format(parse).replace("-", "");
                return sf;
            }
            format = new SimpleDateFormat(datePattern);
            Date parse = format.parse(dateString);
            sf = format.format(parse);
            return sf;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf;
    }

    /**
     * date 的 pattern修改
     *
     * @param dateString String时间字符串
     * @return Date
     */
    public static Date timeCreator(String dateString) {
        Date parse = null;
        try {
            parse = parseDate(dateString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

    private static Date convertToDate(LocalDateTime localDateTime) {
        return java.sql.Timestamp.valueOf(localDateTime);
    }

    private static Map<String, SimpleDateFormat> parserPool = new HashMap<>();

    static {
        getParser("yyyy-MM-dd HH:mm:ss");
        getParser("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        getParser("yyyy-MM-dd HH:mm:ss.SSS");
        getParser("yyyy-MM-dd HH:mm");
        getParser("yyyy-MM-dd hh:mm a");
        getParser("yyyyMMddHHmmss");
        getParser("yyyyMMdd HHmmss");
        getParser("yyyy/MM/dd HH:mm:ss");
        getParser("yyyy/MM/dd");
        getParser("yyyy-MM-dd");
        // 添加其他可能的日期格式
    }

    public static SimpleDateFormat getParser(String pattern) {
        if (!parserPool.containsKey(pattern)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            parserPool.put(pattern, sdf);
        }
        return parserPool.get(pattern);
    }

    public static synchronized void returnParser(String pattern, SimpleDateFormat sdf) {
        // Optional: You can perform cleanup or additional actions before returning the parser to the pool.
        // In this simple example, we do nothing special.
    }

    public static Date parseDate(String dateString) throws ParseException {
        for (String pattern : parserPool.keySet()) {
            SimpleDateFormat sdf = getParser(pattern);
            try {
                return sdf.parse(dateString);
            } catch (ParseException ignored) {

            }
        }
        throw new ParseException("无法解析时间字符串:" + dateString, 0);
    }

    /**
     * LocalDateTime 转 Date
     *
     * @return
     * @throws ParseException
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault()); // 使用系统默认时区
        Date date = Date.from(zonedDateTime.toInstant()); // 转换为Instant，再转换为Date
        return date;
    }

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant(); // 转换为Instant
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault()); // 使用系统默认时区
        return zdt.toLocalDateTime(); // 转换为LocalDateTime
    }

    public static Date parseDateByHutool(String dateString) throws ParseException {
        for (String pattern : parserPool.keySet()) {
            SimpleDateFormat sdf = getParser(pattern);
            try {
                return DateUtil.parse(dateString, sdf.toString());
            } catch (DateException ignored) {

            }
        }
        throw new ParseException("无法解析时间字符串:" + dateString, 0);
    }


}
