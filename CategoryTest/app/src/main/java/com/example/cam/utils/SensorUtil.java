package com.example.cam.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

/**
 * Created by MoreSmart-PC007 on 2016/3/25.
 */
public class SensorUtil {

    public static float Lux = 0f;

    public static float getCurLux(Context context) {
        //获取SensorManager对象
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //获取Sensor对象
        Sensor ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        MySensorListener listener = new MySensorListener();
        sm.registerListener(listener, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
        try {
            return Lux;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sm.unregisterListener(listener);
        }
        return  Lux;
    }

    public static class MySensorListener implements SensorEventListener {

        private float lux = 0f;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
        public void onSensorChanged(SensorEvent event) {
            //获取精度
//            float acc = event.accuracy;
            //获取光线强度
            Lux = event.values[0];
            lux = event.values[0];
        }

        public float getLux() {
            return lux;
        }
    }

    public static float[] getAccXYZ(Context context) {
        SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        AccListener listener = new AccListener();
        Sensor AccSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(listener,AccSensor,SensorManager.SENSOR_DELAY_NORMAL);
        try {
            return listener.mAcc;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sm.unregisterListener(listener);
        }
        return new float[3];
    }

    public static class AccListener implements SensorEventListener {

        public float[] mAcc = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            mAcc[0] = event.values[0];
            mAcc[1] = event.values[1];
            mAcc[2] = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public float[] getmAcc() {
            return mAcc;
        }
    }
}
