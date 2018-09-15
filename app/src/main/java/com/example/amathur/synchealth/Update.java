package com.example.amathur.synchealth;

import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.ArrayList;
import java.util.List;

public class Update {

    static Samsung samsung;
    static Google googleFit;

    static void update(){
        try {
            List<HealthData> samsungstepsdata = Update.samsung.getAllStepsDataPoints();
            List<HealthData> samsungheartdata = Update.samsung.getAllHRDataPoints();
            update_stepcount(samsungstepsdata);
            update_hr(samsungheartdata);
            update_calorie(samsungstepsdata);
            update_speed(samsungstepsdata);
            update_distance(samsungstepsdata);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static void update_stepcount(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertstepsStoGdata(data);
            googledata.add(d);
        }
        googleFit.addStepCountData(googledata);
    }

    static void update_distance(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertDistancetoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addDistanceData(googledata);
    }

    static void update_speed(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertSpeedtoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addSpeedData(googledata);
    }

    static void update_calorie(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertCalorietoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addCalorieData(googledata);
    }


    static void update_hr(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertHRStoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addHeartRateData(googledata);
    }
}
