package com.example.cam.server;

/**
 * Created by cam on 1/11/16.
 */
public class ReceviceObject {
    private String packName;
    private long receviceTime;
    private int receviceCount;

    public ReceviceObject() {}

    public ReceviceObject(String packName, long receviceTime) {
        this.packName = packName;
        this.receviceTime = receviceTime;
        receviceCount = 0;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public long getReceviceTime() {
        return receviceTime;
    }

    public void setReceviceTime(long receviceTime) {
        this.receviceTime = receviceTime;
    }

    public int getReceviceCount() {
        return receviceCount;
    }

    public void setReceviceCount(int receviceCount) {
        this.receviceCount = receviceCount;
    }
}
