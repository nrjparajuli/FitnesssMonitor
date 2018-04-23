package com.example.nirajparajuli0.fitnessmonitoring;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static List<Float> x;
    private static List<Float> y;
    private static List<Float> z;
    private static List<Float> input_signal;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private RecognitionActivity activityPrediction;

    private boolean confidenceView = false;
    private TextView downstairsTextView;
    private TextView joggingTextView;
    private TextView sittingTextView;
    private TextView standingTextView;
    private TextView upstairsTextView;
    private TextView walkingTextView;

    private AppDatabase activityDB;
    private Activity activity;
    private String current_activity = Constants.JOGGING;
    private int activity_duration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x = new ArrayList<Float>();
        y = new ArrayList<Float>();
        z = new ArrayList<Float>();
        input_signal = new ArrayList<Float>();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        activityPrediction = new RecognitionActivity(getApplicationContext());
        activityDB = AppDatabase.getAppDatabase(MainActivity.this);
    }

    protected void onPause() {
        super.onPause();
        confidenceView = false;
    }

    protected void onResume() {
        super.onResume();
        confidenceView = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        activityPrediction();
        x.add(event.values[0]);
        y.add(event.values[1]);
        z.add(event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private void activityPrediction()
    {
        if(x.size() == Constants.N_SAMPLES && y.size() == Constants.N_SAMPLES && z.size() == Constants.N_SAMPLES) {
            normalizeBySD();
            input_signal.addAll(x); input_signal.addAll(y); input_signal.addAll(z);
            float[] results = activityPrediction.getActivityProb(toFloatArray(input_signal));

            if (confidenceView) {
                walkingTextView.setText(Float.toString(round(results[0], 2)));
                joggingTextView.setText(Float.toString(round(results[1], 2)));
                sittingTextView.setText(Float.toString(round(results[2], 2)));
                standingTextView.setText(Float.toString(round(results[3], 2)));
                upstairsTextView.setText(Float.toString(round(results[4], 2)));
                downstairsTextView.setText(Float.toString(round(results[5], 2)));
            }

            long unixTime = System.currentTimeMillis() / 1000L;
            String[] activity_results = predictedActivity(results);
            String activity_type = activity_results[1];
            int activity_indx = Integer.parseInt(activity_results[0]);

            if (results[activity_indx] > 0.7) {
                if (confidenceView) {
                    TableLayout tableLayout = (TableLayout) findViewById(R.id.TableConfidence);
                    int drawableId = getResources().getIdentifier(activity_type.toLowerCase(), "drawable", getPackageName());
                    Log.d("Converted case ", activity_type);
                    tableLayout.setBackgroundResource(drawableId);
                }

                activity_duration += 1;

                if (current_activity != activity_type || activity_duration >= 1000) {
                    Log.d(current_activity, Integer.toString(activity_duration));
                    activity = new Activity(unixTime, current_activity, activity_duration);
                    activity_duration = 0;
                    current_activity = activity_type;
                    new InsertActivity(MainActivity.this, activity).execute();
                }
            }

            x.clear(); y.clear(); z.clear(); input_signal.clear();
        }
    }

    private float[] toFloatArray(List<Float> list)
    {
        int i = 0;
        float[] array = new float[list.size()];

        for (Float f : list) {
            array[i++] = (f != null ? f : Float.NaN);
        }
        return array;
    }

    private void normalizeBySD()
    {
        for(int i = 0; i < Constants.N_SAMPLES; i++)
        {
            x.set(i,((x.get(i) - Constants.MEAN_X)/Constants.SD_X));
            y.set(i,((y.get(i) - Constants.MEAN_Y)/Constants.SD_Y));
            z.set(i,((z.get(i) - Constants.MEAN_Z)/Constants.SD_Z));
        }
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public String[] predictedActivity(float[] results) {
        int predictedLabel = 0;
        String predictedString;

        for (int i = 0; i < 6; i++) {
            if (results[predictedLabel] < results[i]) {
                predictedLabel = i;
            }
        }

        switch (predictedLabel) {
            case 0:
                predictedString = Constants.WALKING;
                break;
            case 1:
                predictedString = Constants.JOGGING;
                break;
            case 2:
                predictedString = Constants.SITTING;
                break;
            case 3:
                predictedString = Constants.STANDING;
                break;
            case 4:
                predictedString = Constants.UPSTAIRS;
                break;
            case 5:
                predictedString = Constants.DOWNSTAIRS;
                break;
            default:
                predictedString = "";
                break;
        }

        String[] result = {Integer.toString(predictedLabel), predictedString};
        return result;
    }

    public void viewTable(View view){
        confidenceView = true;
        Log.d("Boolean Value ", Boolean.toString(confidenceView));

        setContentView(R.layout.view_confidence);

        downstairsTextView = (TextView)findViewById(R.id.downstairs_prob);
        joggingTextView = (TextView)findViewById(R.id.jogging_prob);
        sittingTextView = (TextView)findViewById(R.id.sitting_prob);
        standingTextView = (TextView)findViewById(R.id.standing_prob);
        upstairsTextView = (TextView)findViewById(R.id.upstairs_prob);
        walkingTextView = (TextView)findViewById(R.id.walking_prob);
    }

    public void viewStats(View view) {
        Intent intent = new Intent(this, DisplayStatsActivity.class);
        startActivity(intent);
    }

    public void startAR(View view) {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        confidenceView = false;
    }

    public void stopAR(View view) {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void goBack(View view){
        confidenceView = false;
        setContentView(R.layout.activity_main);
    }

    private static class InsertActivity extends AsyncTask<Void,Void,Integer> {
        private WeakReference<MainActivity> activityReference;
        private Activity activity; // only retain a weak reference to the activity
        InsertActivity(MainActivity context, Activity activity) {
            activityReference = new WeakReference<>(context);
            this.activity = activity;
        }

        @Override protected Integer doInBackground(Void... objs) {
            activityReference.get().activityDB.activityDao().insert(activity);
            return activityReference.get().activityDB.activityDao().countEntries();
        }

        @Override protected void onPostExecute(Integer count) {
            String countStr = Integer.toString(count);
            Log.d("Success", "Added to DB " + countStr);
        }
    }
}
