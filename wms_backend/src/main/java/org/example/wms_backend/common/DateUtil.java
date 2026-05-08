package org.example.wms_backend.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期为字符串
     * @param date 日期对象
     * @param format 日期格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化日期为字符串（yyyy-MM-dd）
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
        return format(date, DATE_FORMAT);
    }

    /**
     * 格式化日期时间为字符串（yyyy-MM-dd HH:mm:ss）
     * @param date 日期对象
     * @return 格式化后的字符串
     */
    public static String formatDateTime(Date date) {
        return format(date, DATETIME_FORMAT);
    }

    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前日期字符串（yyyy-MM-dd）
     * @return 当前日期字符串
     */
    public static String getCurrentDateStr() {
        return formatDate(getCurrentDate());
    }

    /**
     * 获取当前日期时间字符串（yyyy-MM-dd HH:mm:ss）
     * @return 当前日期时间字符串
     */
    public static String getCurrentDateTimeStr() {
        return formatDateTime(getCurrentDate());
    }
}
