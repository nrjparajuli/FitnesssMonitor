package com.example.nirajparajuli0.fitnessmonitoring;

import android.content.Context;
import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class RecognitionActivity {
    static {
        System.loadLibrary(Constants.LIB_NAME);
    }

    private static RecognitionActivity activityInferenceInstance;
    private TensorFlowInferenceInterface inferenceInterface;
    private static AssetManager assetManager;

    public static RecognitionActivity getInstance(final Context context)
    {
        if (activityInferenceInstance == null)
        {
            activityInferenceInstance = new RecognitionActivity(context);
        }
        return activityInferenceInstance;
    }

    public RecognitionActivity(final Context context) {
        this.assetManager = context.getAssets();
        inferenceInterface = new TensorFlowInferenceInterface(assetManager, Constants.MODEL_FILE);
    }

    public float[] getActivityProb(float[] input_signal)
    {
        float[] result = new float[Constants.OUTPUT_SIZE];
        inferenceInterface.feed(Constants.INPUT_NODE,input_signal,Constants.INPUT_SIZE);
        inferenceInterface.run(Constants.OUTPUT_NODES);
        inferenceInterface.fetch(Constants.OUTPUT_NODE,result);
        return result;
    }
}
