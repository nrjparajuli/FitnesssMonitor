package com.example.nirajparajuli0.fitnessmonitoring;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = Constants.TABLE_NAME)
public class Activity {

    @PrimaryKey(autoGenerate = true)
    int uid;

    @ColumnInfo(name = Constants.COL1_NAME)
    long startTimestamp;

    @ColumnInfo(name = Constants.COL2_NAME)
    String activityType;

    @ColumnInfo(name = Constants.COL3_NAME)
    int duration;

    public Activity(long startTimestamp, String activityType, int duration) {
        this.startTimestamp = startTimestamp;
        this.activityType = activityType;
        this.duration = duration;
    }

    public int getUid() {
        return uid;
    }

//    public void setUid(int uid) {
//        this.uid = uid;
//    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

//    public void setStartTimestamp(int startTimestamp) {
//        this.startTimestamp = startTimestamp;
//    }

    public String getActivityType() {
        return activityType;
    }

//    public void setActivityType(String activity) {
//        this.activityType = activity;
//    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}