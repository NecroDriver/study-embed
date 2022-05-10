package com.xin.web.utils.date;

import com.xin.web.utils.convert.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理工具列
 *
 * @author creator mafh 2019/11/26 17:19
 * @author updater
 * @version 1.0.0
 */
public class DateUtils {

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 格式化时间 格式: yyyy-MM-dd HH:mm:ss
     *
     * @param date      日期
     * @param formatStr 格式化字符串
     * @return String 结果
     */
    public static String formatDate(Date date, String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            return date == null ? null : format.format(date);
        } catch (Exception e) {
            logger.error("日期转换日常", e);
            return null;
        }
    }

    /**
     * 获取当前日期时间 格式:参数 例如 yyyy-MM-dd HH:mm:ss
     *
     * @param timeStr   时间字符串
     * @param formatStr 格式化字符串
     * @return 时间
     */
    public static Date getDateTime(String timeStr, String formatStr) {
        try {
            DateFormat format1 = new SimpleDateFormat(formatStr);
            return format1.parse(timeStr);
        } catch (ParseException e) {
            logger.error("日期转换日常", e);
            return null;
        }
    }
}
