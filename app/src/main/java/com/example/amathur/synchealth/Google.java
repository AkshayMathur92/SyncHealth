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

import static com.google.android.gms.fitness.data.DataSource.TYPE_RAW;

public class Google {

    //Google
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    String APP_TAG = "SYNCHEALTH-GOOGLE";
    Activity mInstance;
    HistoryClient historyClient;
    public static DataSource stepdataSource ;
    public static DataSource heartrateDataSource;

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
                    .build();

            if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(mInstance), fitnessOptions)) {
                GoogleSignIn.requestPermissions(
                        mInstance, // your activity
                        0533,
                        GoogleSignIn.getLastSignedInAccount(mInstance),
                        fitnessOptions);
            }
            historyClient = Fitness.getHistoryClient(mInstance, GoogleSignIn.getLastSignedInAccount(mInstance));

            stepdataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                    .setType(TYPE_RAW)
                    .build();

            heartrateDataSource = (new DataSource.Builder())
                    .setAppPackageName(mInstance)
                    .setDataType(DataType.TYPE_HEART_RATE_BPM)
                    .setType(TYPE_RAW)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStepCountData(List<DataPoint> googledata) {

        if(googledata.size() > 1000){
            int total_steps = 0;
            List<List<DataPoint>> list_list_dp = getBatches(googledata, 1000);
            for(List<DataPoint> ldp : list_list_dp){
                DataSet dataSet = DataSet.create(stepdataSource);
                for(DataPoint d : googledata){
                    try {
                        dataSet.add(d);
                        total_steps += d.getValue(Field.FIELD_STEPS).asInt();
                    }catch (IllegalArgumentException e){
                        Log.d(APP_TAG, e.getMessage() + " with Steps = " + d.getValue(Field.FIELD_STEPS).asInt());
                    }
                }
                Log.d(APP_TAG, "Total Steps adding in Google Data Points = " + total_steps);
                Task<Void> response = historyClient.insertData(dataSet);
                response.addOnCompleteListener(task -> Log.d(APP_TAG, "Response of adding individual steps " + ((task.isSuccessful())?"true" : task.getException().toString())));
            }
        }
        else{
            int total_steps = 0;
            DataSet dataSet = DataSet.create(stepdataSource);
            for(DataPoint d : googledata){
                try {
                    dataSet.add(d);
                    total_steps += d.getValue(Field.FIELD_STEPS).asInt();
                }catch (IllegalArgumentException e){
                    Log.d(APP_TAG, e.getMessage() + " with Steps = " + d.getValue(Field.FIELD_STEPS).asInt());
                }
            }
            Log.d(APP_TAG, "Total Steps adding in Google Data Points = " + total_steps);
            Task<Void> response = historyClient.insertData(dataSet);
            response.addOnCompleteListener(task -> Log.d(APP_TAG, "Response of adding individual steps = " + task.isSuccessful() ));
        }
    }

    public void addHeartRateData(List<DataPoint> googledata) {

        if(googledata.size() > 1000){
            List<List<DataPoint>> list_list_dp = getBatches(googledata, 1000);
            for(List<DataPoint> ldp : list_list_dp){
                DataSet dataSet = DataSet.create(heartrateDataSource);
                for(DataPoint d : googledata){
                    try {
                        dataSet.add(d);
                    }catch (IllegalArgumentException e){
                        Log.d(APP_TAG, "Adding Heart Rate Data point exception " + e.getMessage() );
                    }
                }
                Task<Void> response = historyClient.insertData(dataSet);
                response.addOnCompleteListener(task -> Log.d(APP_TAG, "Response of adding HR data " + ((task.isSuccessful())?"true" : task.getException().toString())));
            }
        }
        else{
            DataSet dataSet = DataSet.create(heartrateDataSource);
            for(DataPoint d : googledata){
                try {
                    dataSet.add(d);
                }catch (IllegalArgumentException e){
                    Log.d(APP_TAG, "Adding Heart Rate Data point exception " + e.getMessage() );
                }
            }
            Task<Void> response = historyClient.insertData(dataSet);
            response.addOnCompleteListener(task -> Log.d(APP_TAG, "Response of adding HR data " + ((task.isSuccessful())? "true" : task.getException().toString())));
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
