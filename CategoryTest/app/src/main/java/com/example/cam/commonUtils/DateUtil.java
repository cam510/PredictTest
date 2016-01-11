package com.example.cam.commonUtils;

import com.example.cam.DB.TableIndex;

import java.text.SimpleDateFormat;

/**
 * Created by cam on 1/7/16.
 */
public class DateUtil {

    public static String[] dataArray = new String[] {
            TableIndex.Period._0_1,
            TableIndex.Period._1_2,
            TableIndex.Period._2_3,
            TableIndex.Period._3_4,
            TableIndex.Period._4_5,
            TableIndex.Period._5_6,
            TableIndex.Period._6_7,
            TableIndex.Period._7_8,
            TableIndex.Period._8_9,
            TableIndex.Period._9_10,
            TableIndex.Period._10_11,
            TableIndex.Period._11_12,
            TableIndex.Period._12_13,
            TableIndex.Period._13_14,
            TableIndex.Period._14_15,
            TableIndex.Period._15_16,
            TableIndex.Period._16_17,
            TableIndex.Period._17_18,
            TableIndex.Period._18_19,
            TableIndex.Period._19_20,
            TableIndex.Period._20_21,
            TableIndex.Period._21_22,
            TableIndex.Period._22_23,
            TableIndex.Period._23_24};

    public static String formatDateWithoutHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }

    public static String formatDateWithHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return df.format(date);
    }

    public static String formatDateWithHourMin(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        return df.format(date);
    }

    public static String formatDateWithHourMinSecond(long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }

    public static String formatHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        return df.format(date);
    }

    public static int toHour(long date) {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String sHour = df.format(date);
        int hour = 0;
        try {
            hour = Integer.parseInt(sHour);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  hour;
    }

}
