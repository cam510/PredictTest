package com.example.cam.commonUtils;

import java.text.SimpleDateFormat;

/**
 * Created by cam on 1/7/16.
 */
public class DateUtil {

    public static String formatDateWithoutHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }

    public static String formatDateWithHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return df.format(date);
    }

    public static String formatDateWithHourMIN(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        return df.format(date);
    }

    public static String formatHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        return df.format(date);
    }

}
