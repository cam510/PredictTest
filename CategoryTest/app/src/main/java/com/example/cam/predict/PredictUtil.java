package com.example.cam.predict;

import android.content.Context;

import com.example.cam.DB.DatabaseHelper;
import com.example.cam.DB.TableIndex;
import com.example.cam.MyApplication;
import com.example.cam.commonUtils.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cam on 1/26/16.
 */
public class PredictUtil {

    private MyApplication context;

    public static PredictUtil mInstance;

    private DatabaseHelper mDbHelper;

    private HashMap<String, PredictBean> mData;

    WeakHashMap<String, Integer> curLocationMap = new WeakHashMap<String, Integer>();
    WeakHashMap<String, Integer> curTimeMap = new WeakHashMap<String, Integer>();
    WeakHashMap<String, PredictBean> allData = new WeakHashMap<String, PredictBean>();

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
        ArrayList<DataBean> nextAppData = mDbHelper.getNextAppNew(packName);
        String curTime = DateUtil.dataArray[DateUtil.toHour(System.currentTimeMillis())];

        PredictBean max = new PredictBean("", 0, 0);


        if (curTimeMap == null) {
            curTimeMap = new WeakHashMap<String, Integer>();
        }
        if (curLocationMap == null) {
            curLocationMap = new WeakHashMap<String, Integer>();
        }

        allData.clear();
        curLocationMap.clear();
        curTimeMap.clear();

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
                if (curTimeMap.containsKey(d.getAppName())) {
                    int count = curTimeMap.get(d.getAppName());
                    count++;
                    curTimeMap.put(new String(d.getAppName()), count); //new String () advoice OOM
                } else {
                    curTimeMap.put(new String(d.getAppName()), 1);
                }
            }
            if (MyApplication.getLocationType().equals(d.getLocationType())) {
                curLocationCount++;
                if (curLocationMap.containsKey(new String(d.getAppName()))) {
                    int count = curLocationMap.get(new String(d.getAppName()));
                    count++;
                    curLocationMap.put(new String(d.getAppName()), count);
                } else {
                    curLocationMap.put(new String(d.getAppName()), 1);
                }
            }
            if (allData.containsKey(d.getAppName())) {
                PredictBean p = allData.get(d.getAppName());
                int lanucherCount = p.getLanucherCount()+1;
                p.setLanucherCount(lanucherCount);
                p.setProbability(lanucherCount * 100 / allDataSize);
                allData.put(d.getAppName(), p);
            }else {
                PredictBean p = new PredictBean(d.getAppName(), 1, 0);
                allData.put(d.getAppName(), p);
            }
        }

        Iterator iter = allData.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            PredictBean p = (PredictBean) entry.getValue();
            String appName = p.getPackName();
            int p_time = curTimeMap.get(appName) * 100 / curTimeCount; //当前时间app的概率
            int p_location = curLocationMap.get(appName) * 100 /curLocationCount; //当前位置app的概率
            int curProbability = p.getProbability();
            if (p_time * p_location * curProbability > max.getProbability()) {
                max.setProbability(p_time * p_location * curProbability);
                max.setPackName(appName);
            }
        }
        System.out.println("may be the next app " + max.getPackName());
        System.gc();
    }

}
