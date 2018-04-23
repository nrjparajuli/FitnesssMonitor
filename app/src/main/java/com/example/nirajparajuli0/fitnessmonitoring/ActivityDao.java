package com.example.nirajparajuli0.fitnessmonitoring;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {

    @Query("SELECT min(start_timestamp) as start_timestamp, activity_type, sum(duration) as duration FROM activity " +
            "where start_timestamp >= :start_timestamp " +
            "GROUP BY activity_type")
    List<Activity> getProportion(long start_timestamp);

    @Query("SELECT COUNT(*) from activity")
    int countEntries();

    @Insert
    void insert(Activity activity);
}
