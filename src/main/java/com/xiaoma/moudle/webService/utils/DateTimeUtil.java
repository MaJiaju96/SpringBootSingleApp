package com.xiaoma.moudle.webService.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeUtil {
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DateTimeUtil() {
    }

    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMAT.format(temporal);
    }

    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMAT.format(temporal);
    }

    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMAT.format(temporal);
    }

    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(temporal);
    }

    public static TemporalAccessor parse(String dateStr, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return format.parse(dateStr);
    }

    public static TemporalAccessor parse(String dateStr, DateTimeFormatter formatter) {
        return formatter.parse(dateStr);
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(String value) {
        if (StringUtil.isNotBlank(value)) {
            int length = value.length();
            if ("yyyy-MM-dd".length() == length) {
                LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDateTime.from(localDate.atStartOfDay());
            } else if ("yyyy-MM-dd hh:mm:ss".length() == length) {
                return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if ("yyyy-MM".length() == length) {
                YearMonth yearMonth = YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyy-MM"));
                return LocalDateTime.from(yearMonth.atDay(1).atStartOfDay());
            } else if ("yyyy".length() == length) {
                Year year = Year.parse(value, DateTimeFormatter.ofPattern("yyyy"));
                return LocalDateTime.from(year.atDay(1).atStartOfDay());
            }
        }
        return null;
    }
}
