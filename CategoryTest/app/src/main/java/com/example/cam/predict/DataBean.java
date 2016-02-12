package com.example.cam.predict;

/**
 * Created by cam on 2/11/16.
 */
public class DataBean{
    private String timePeriod;
    private String locationType;
    private String appName;
    private int count;

    public DataBean(String timePeriod, String locationType, String appName, int count) {
        this.timePeriod = timePeriod;
        this.locationType = locationType;
        this.appName = appName;
        this.count = count;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
