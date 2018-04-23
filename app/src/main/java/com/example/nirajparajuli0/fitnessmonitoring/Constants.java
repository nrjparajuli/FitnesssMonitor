package com.example.nirajparajuli0.fitnessmonitoring;

import android.graphics.Color;

public class Constants {

    // MainActivity.java
    public static final int N_SAMPLES = 90;
    public static final float MEAN_X = 0.662868f;
    public static final float MEAN_Y = 7.255639f;
    public static final float MEAN_Z = 0.411062f;
    public static final float SD_X = 6.849058f;
    public static final float SD_Y = 6.746204f;
    public static final float SD_Z = 4.754109f;
    public static final String WALKING = "Walking";
    public static final String SITTING = "Sitting";
    public static final String JOGGING = "Jogging";
    public static final String STANDING = "Standing";
    public static final String UPSTAIRS = "Upstairs";
    public static final String DOWNSTAIRS = "Downstairs";

    // Activity.java
    public static final String TABLE_NAME = "activity";
    public static final String COL1_NAME = "start_timestamp";
    public static final String COL2_NAME = "activity_type";
    public static final String COL3_NAME = "duration";

    // AppDatabase.java
    public static final String DB_NAME = "activity_database";

    // DisplayStatsActivity.java
    public static final String EMPTY = "";
    public static final String OPTION_HOUR = "Past Hour";
    public static final String OPTION_DAY = "Past Day";
    public static final String OPTION_WEEK = "Past Week";
    public static final String OPTION_MONTH = "Past Month";
    public static final long MILLI_TO_SEC = 1000L;
    public static final int SEC_IN_HOUR = 3600;
    public static final int SEC_IN_DAY = 86400;
    public static final int SEC_IN_WEEK = 604800;
    public static final int SEC_IN_MONTH = 2592000;
    public static final int[] JOYFUL_COLORS = {
            Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
            Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(42, 109, 130)};
    public static final int TO_PERCENT = 100;
    public static final int DP = 1;
    public static final float TEXT_SIZE = 10f;

    // RecognitionActivity.java
    public static final String LIB_NAME = "tensorflow_inference";
    public static final String MODEL_FILE = "file:///android_asset/har_classifier.pb";
    public static final String INPUT_NODE = "input";
    public static final String[] OUTPUT_NODES = {"output"};
    public static final String OUTPUT_NODE = "output";
    public static final long[] INPUT_SIZE = {1,1,90,3};
    public static final int OUTPUT_SIZE = 6;
}
