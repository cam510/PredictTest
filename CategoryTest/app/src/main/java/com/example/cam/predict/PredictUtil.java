package com.example.cam.predict;

import android.content.Context;

import com.example.cam.DB.DatabaseHelper;
import com.example.cam.MyApplication;
import com.example.cam.commonUtils.DateUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by cam on 1/26/16.
 */
public class PredictUtil {

    private MyApplication context;

    public static PredictUtil mInstance;

    private DatabaseHelper mDbHelper;

    private HashMap<String, PredictBean> mData;

    private PredictBean allMax;

    private String today;

    public PredictUtil(MyApplication context) {
        this.context = context;
        mDbHelper = context.getmDbHelper();
        mData = new HashMap<String, PredictBean>();
        today = DateUtil.formatDateWithHourMin(System.currentTimeMillis());
        getOneDayData();
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
}
