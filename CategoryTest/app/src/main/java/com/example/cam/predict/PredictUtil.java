package com.example.cam.predict;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.LruCache;

import com.example.cam.DB.DatabaseHelper;
import com.example.cam.DB.TableIndex;
import com.example.cam.MyApplication;
import com.example.cam.commonUtils.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by cam on 1/26/16.
 */
public class PredictUtil {

    private MyApplication context;

    public static PredictUtil mInstance;

    private DatabaseHelper mDbHelper;

    private HashMap<String, PredictBean> mData;

    LruCache<String, Integer> curLocationMap = new LruCache<>(1024/8);
    LruCache<String, Integer> curTimeMap = new LruCache<String, Integer>(1024/8);
    LruCache<String, PredictBean> allData = new LruCache<String, PredictBean>(1024/2);

    private PredictBean allMax;

    private String today;

    public PredictUtil(MyApplication context) {
        this.context = context;
        mDbHelper = context.getmDbHelper();
//        mData = new HashMap<String, PredictBean>();
        today = DateUtil.formatDateWithHourMin(System.currentTimeMillis());
//        getOneDayData();
    }

    public static PredictUtil getmInstance(MyApplication context) {
        if (mInstance == null) {
            mInstance = new PredictUtil(context);
        }
        return mInstance;
    }

    /**
     * 暂定
     * 获取过去一天的数据
     */
    public void getOneDayData() {
        allMax = new PredictBean("", 0, 0);
        HashMap<String, Integer> lastDayData = mDbHelper.getLastDayApp();
        if (lastDayData == null || lastDayData.size() == 0) {
            return;
        }
        int allLannchur = 0;
        Iterator iter = lastDayData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry.getKey().equals("all")) {
                allLannchur = (int) entry.getValue();
            } else {
                int temp = 0;
                if (allLannchur > 0 ) {
                    temp = (int)entry.getValue()*100/allLannchur;
                }
                PredictBean p = new PredictBean(entry.getKey().toString(), (int)entry.getValue(), temp);
                mData.put(entry.getKey().toString(), p);
                if ((int)entry.getValue() > allMax.getLanucherCount()) {
                    allMax.setPackName(entry.getKey().toString());
                    allMax.setLanucherCount((int) entry.getValue());
                    allMax.setProbability(temp);
                }
            }
        }
    }

    /**
     * 暂定
     * 获取过去一天对应时间段的数据
     */
    public String predictNextApp() {
        String packName = null;
        HashMap<String, Integer> predictData = mDbHelper.getLastDayPeriodApp();
        PredictBean max = new PredictBean("", 0, 0);
        int allLannchur = 0;
        Iterator iter = predictData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry.getKey().equals("all")) {
                allLannchur = (int) entry.getValue();
            } else {
                int temp = 0;
                if (allLannchur > 0 ) {
                    temp = (int)entry.getValue()*100/allLannchur;
                }

                //贝叶斯公式，当前时间段app打开时候数据
                if (temp * mData.get(entry.getKey().toString()).getProbability() > max.getProbability()) {
                    max.setPackName(entry.getKey().toString());
                    max.setLanucherCount((int) entry.getValue());
                    max.setProbability(temp * mData.get(entry.getKey().toString()).getProbability());
                }
            }
        }
        if (predictData.size() == 0) {
            System.out.println("return all max -> " + allMax.getPackName());
            return allMax.getPackName();
        } else {
            System.out.println("return predict max -> " + max.getPackName());
        }
        return max.getPackName();
    }

    public String getNextApp(String appName) {
        PredictBean max = new PredictBean("", 0, 0);
        HashMap<String, Integer> lastDayData = mDbHelper.getNextApp(appName);
        if (lastDayData == null || lastDayData.size() == 0) {
            return "";
        }
        int allLannchur = 0;
        Iterator iter = lastDayData.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry.getKey().equals("all")) {
                allLannchur = (int) entry.getValue();
            } else {
                int temp = 0;
                if (allLannchur > 0 ) {
                    temp = (int)entry.getValue()*100/allLannchur;
                }
                PredictBean p = new PredictBean(entry.getKey().toString(), (int)entry.getValue(), temp);
                if ((int)entry.getValue() > allMax.getLanucherCount()) {
                    max.setPackName(entry.getKey().toString());
                    max.setLanucherCount((int) entry.getValue());
                    max.setProbability(temp);
                }
            }
        }
        return max.getPackName();
    }

    public void getSomeDataFromPackName(String packName) {
//        ArrayList<DataBean> nextAppData = mDbHelper.getNextAppNew(packName);
        ArrayList<DataBean> nextAppData = mDbHelper.getNextAppNew70(packName);
//        String curTime = DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())];
        //测试数据
        String curTime = DateUtil.dataArray[11];

        PredictBean max = new PredictBean("", 0, 0);

        ArrayList<String> appName = new ArrayList<String>();
        if (curTimeMap == null) {
            curTimeMap = new LruCache<String, Integer>(1024/8);
        }
        if (curLocationMap == null) {
            curLocationMap = new LruCache<String, Integer>(1024/8);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            allData.resize(1024);
            curLocationMap.resize(1024);
            curTimeMap.resize(1024);
        }

        int allDataSize = nextAppData.size();
        if (allDataSize == 0) {
            System.out.println("nextAppData.size -> " + nextAppData.size());
            return;
        }

        int curTimeCount = 0;
        int curLocationCount = 0;
        for (DataBean d : nextAppData) {
            if (curTime.equals(d.getTimePeriod())) {
                curTimeCount++;
                if (curTimeMap.get(d.getAppName()) != null) {
                    int count = curTimeMap.get(d.getAppName());
                    count++;
                    curTimeMap.put(new String(d.getAppName()), count); //new String () advoice OOM
                } else {
                    curTimeMap.put(new String(d.getAppName()), 1);
                }
            }
            //获取当前位置
            //测试数据
//            if ("HOME".equals(d.getLocationType())) {
            if (MyApplication.getLocationType().equals(d.getLocationType())) {
                curLocationCount++;
                if (curLocationMap.get(new String(d.getAppName())) != null) {
                    int count = curLocationMap.get(new String(d.getAppName()));
                    count++;
                    curLocationMap.put(new String(d.getAppName()), count);
                } else {
                    curLocationMap.put(new String(d.getAppName()), 1);
                }
            }
            if (allData.get(d.getAppName()) != null) {
                PredictBean p = allData.get(d.getAppName());
                int lanucherCount = p.getLanucherCount()+1;
                p.setLanucherCount(lanucherCount);
                p.setProbability(lanucherCount * 100 / allDataSize);
                allData.put(d.getAppName(), p);
            }else {
                PredictBean p = new PredictBean(d.getAppName(), 1, 0);
                allData.put(d.getAppName(), p);
            }

            if (!appName.contains(d.getAppName())) {
                appName.add(d.getAppName());
            }
        }

//        Iterator iter = allData.entrySet().iterator();
//        while(iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            PredictBean p = (PredictBean) entry.getValue();
//            String appName = p.getPackName();
//            int p_time = curTimeMap.get(appName) * 100 / curTimeCount; //当前时间app的概率
//            int p_location = curLocationMap.get(appName) * 100 /curLocationCount; //当前位置app的概率
//            int curProbability = p.getProbability();
//            if (p_time * p_location * curProbability > max.getProbability()) {
//                max.setProbability(p_time * p_location * curProbability);
//                max.setPackName(appName);
//            }
//        }
        for (String s : appName) {
            PredictBean p = (PredictBean) allData.get(s);
            String name = p.getPackName();
            if (curLocationMap == null || curTimeMap == null) {
                return;
            }
            int p_time = 1; //当前时间app的概率
            if (curTimeMap.get(name) != null && curTimeCount > 0) {
                p_time = curTimeMap.get(s) * 100 / curTimeCount;
            }
            int p_location = 1; //当前位置app的概率
            if (curLocationMap.get(name) != null && curLocationCount > 0) {
                p_location = curLocationMap.get(s) * 100 /curLocationCount;
            }
            int curProbability = p.getProbability();
            if (p_time * p_location * curProbability > max.getProbability()) {
                max.setProbability(p_time * p_location * curProbability);
                max.setPackName(s);
            }
        }
        System.out.println("may be the next app " + max.getPackName());
        System.gc();
    }

//    class MyMap implements Map {
//
//    }
    class MyLruCache extends LruCache {

        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public MyLruCache(int maxSize) {
            super(maxSize);
        }
    }
}
