package com.example.amathur.synchealth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.HistoryClient;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.google.android.gms.fitness.data.DataSource.TYPE_RAW;

public class Google {

    //Google
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    String APP_TAG = "SYNCHEALTH-GOOGLE";
    Activity mInstance;
    HistoryClient historyClient;
    public static DataSource stepDataSource;
    public static DataSource heartrateDataSource;
    public static DataSource calorieDataSource;
    public static DataSource distanceDataSource;
    public static DataSource speedDataSource;

    Google(Activity mInstance, Bundle savedInstanceState){
        this.mInstance = mInstance;
        try {

            if (savedInstanceState != null) {
                authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
            }

            FitnessOptions fitnessOptions = FitnessOptions.builder()
                    .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_WRITE)
                    .addDataType(DataType.TYPE_SPEED, FitnessOptions.ACCESS_WRITE)
                    .build();

            if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(mInstance), fitnessOptions)) {
                GoogleSignIn.requestPermissions(
                        mInstance, // your activity
                        0533,
                        GoogleSignIn.getLastSignedInAccount(mInstance),
                        fitnessOptions);
            }
            historyClient = Fitness.getHistoryClient(mInstance, GoogleSignIn.getLastSignedInAccount(mInstance));

            stepDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                    .setType(TYPE_RAW)
                    .build();

            heartrateDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_HEART_RATE_BPM)
                    .setType(TYPE_RAW)
                    .build();

            calorieDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_CALORIES_EXPENDED)
                    .setType(TYPE_RAW)
                    .build();

            distanceDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_DISTANCE_DELTA)
                    .setType(TYPE_RAW)
                    .build();

            speedDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_SPEED)
                    .setType(TYPE_RAW)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStepCountData(List<DataPoint> googledata) {
        addData(googledata, stepDataSource, " Step Count ");
    }

    public void addHeartRateData(List<DataPoint> googledata) {
        addData(googledata, heartrateDataSource, " Heart Rate ");
    }

    public void addDistanceData(List<DataPoint> googledata) {
        addData(googledata, distanceDataSource, " Distance ");
    }

    public void addCalorieData(List<DataPoint> googledata) {
        addData(googledata, calorieDataSource, " Calorie ");
    }

    public void addSpeedData(List<DataPoint> googledata) {
        addData(googledata, speedDataSource, " Speed ");
    }

    public void addData(List<DataPoint> googledata, DataSource dataSource, String message){
        List<List<DataPoint>> list_list_dp;
        if(googledata.size() > 1000) {
            list_list_dp = getBatches(googledata, 1000);
        }
        else{
            list_list_dp = new ArrayList<>();
            list_list_dp.add(googledata);
        }
        for(List<DataPoint> ldp : list_list_dp) {
            DataSet dataSet = DataSet.create(dataSource);
            for (DataPoint d : ldp) {
                try {
                    dataSet.add(d);
                } catch (IllegalArgumentException e) {
                    Log.d(APP_TAG, message + " Data point exception " + e.getMessage());
                }
            }
            Task<Void> response = historyClient.insertData(dataSet);
            response.addOnCompleteListener(task -> Log.d(APP_TAG, "Response of adding " + message + ((task.isSuccessful())? "true" : task.getException().toString())));
        }
    }

    public static <T> List<List<T>> getBatches(List<T> collection,int batchSize){
        int i = 0;
        List<List<T>> batches = new ArrayList<>();
        while(i<collection.size()){
            int nextInc = Math.min(collection.size()-i,batchSize);
            List<T> batch = collection.subList(i,i+nextInc);
            batches.add(batch);
            i = i + nextInc;
        }
        return batches;
    }
}
