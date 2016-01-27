package com.example.cam.predict;

/**
 * Created by cam on 1/26/16.
 */
public class PredictBean {

    private String packName;
    private int lanucherCount;
    private int probability = 0;

    public PredictBean(String packName, int lanucherCount) {
        this.packName = packName;
        this.lanucherCount = lanucherCount;
    }

    public PredictBean(String packName, int lanucherCount, int probability) {
        this.packName = packName;
        this.lanucherCount = lanucherCount;
        this.probability = probability;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getLanucherCount() {
        return lanucherCount;
    }

    public void setLanucherCount(int lanucherCount) {
        this.lanucherCount = lanucherCount;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
