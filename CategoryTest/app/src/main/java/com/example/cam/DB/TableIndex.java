package com.example.cam.DB;

/**
 * Created by cam on 1/7/16.
 */
public class TableIndex {

    public static class App {
        public final static String TABLE_NAME = "AppTable";
        public final static String APP_NAME = "AppName";
        public final static String APP_PACKAGE = "AppPackage";
        public final static String APP_CATEGROY = "AppCategroy";
        public final static String APP_ALL_LAUNCHER_COUNT = "count";
    }

    public static class Session {
//        public final static String APP_PACKAGE = "AppPackage";
        public final static String LOCATION = "Location";
        public final static String OPEN_TIME = "OpenTime";
        public final static String TIME_PERIOD = "TimePeriod";
        public final static String ID = "id";
        public final static String NOW_APP = "NowName";
        public final static String NEXT_APP = "NextName";
//        public final static String TODAY_USE_TIME = "TodayUseTime";
//        public final static String TODAY_APP_POWER = "TodayAppPower";
    }

    public static class Notiication{
        public final static String TABLE_NAME = "AppNotiication";
        public final static String APP_PACKAGE = "AppPackage";
        public final static String RECEIVE_TIME = "ReceiveTime";
        public final static String OPEN_TIME = "OpenTime";
    }

    public static class Intimate{
        public final static String TABLE_NAME = "IntimateTable";
        public final static String APP_PACKAGE = "AppPackage";
//        public final static String APP_TYPE = "AppType";
        public final static String INTIMACY = "Intimacy";
        public final static String RECEIVE_COUNT = "ReceiveCount";
    }

    public static class Period{
//        public final static String TABLE_NAME = "PeriodTable";
        public final static String APP_PACKAGE = "AppPackage";
        public final static String _0_1 = "_0_1";
        public final static String _1_2 = "_1_2";
        public final static String _2_3 = "_2_3";
        public final static String _3_4 = "_3_4";
        public final static String _4_5 = "_4_5";
        public final static String _5_6 = "_5_6";
        public final static String _6_7 = "_6_7";
        public final static String _7_8 = "_7_8";
        public final static String _8_9 = "_8_9";
        public final static String _9_10 = "_9_10";
        public final static String _10_11 = "_10_11";
        public final static String _11_12 = "_11_12";
        public final static String _12_13 = "_12_13";
        public final static String _13_14 = "_13_14";
        public final static String _14_15 = "_14_15";
        public final static String _15_16 = "_15_16";
        public final static String _16_17 = "_16_17";
        public final static String _17_18 = "_17_18";
        public final static String _18_19 = "_18_19";
        public final static String _19_20 = "_19_20";
        public final static String _20_21 = "_20_21";
        public final static String _21_22 = "_21_22";
        public final static String _22_23 = "_22_23";
        public final static String _23_24 = "_23_24";
    }

    public static class Location{
        public final static String TABLE_NAME = "LocationTable";
        public final static String ADDRESS = "Address";
        public final static String ADDRESS_TYPE = "Address_Type";
    }

    public static String[] S_ADDRESS_TYPE = new String[] {"HOME", "COMPANY", "SCHOOL",
            "ON_WAY_TO_WORK", "ELSE"};

    public static class NewRecore{
        public final static String ID = "_id";
        public final static String TABLE_NAME = "NewRecore";
        public final static String PACKAGE_NAME = "Package_Name";
        public final static String USE_TIME = "Use_Tiem";          //使用时间
        public final static String USE_PERIOD = "Use_Period";      //使用区间
        public final static String USE_SECOND = "Use_Second";       //记录使用的时间
        public final static String LOCATION_LA = "Location_La";    //经度
        public final static String LOCATION_LO = "Location_Lo";    //维度
        public final static String IS_WORK = "IsWork";            //是否工作日  //0工作日 1非工作日
        public final static String GPRS = "Gprs";                   //是否打开gprs 0未打开  1 打开
        public final static String WIFI = "WIFI";                   //连接wifi名字
        public final static String BLUETOOTH = "Bluetooth";         //是否打开蓝牙 0未打开  1 打开
        public final static String LIGHT_SENSOR  = "Light_Sensor";  //光传感器
        public final static String Notification = "Notification";    //是否关注  0默认非通知栏收到 1急 2一般 3 忽略
        public final static String WEEKDAY = "Weekday";              //记录星期几
        public final static String HEADPHONE = "Head_Phone";          //是否有耳机
        public final static String ACTION = "action";          //是否有耳机
    }
}
