package com.example.amathur.synchealth;

import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class Update {

    static Samsung samsung;
    static Google googleFit;
    private static long last_update;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        last_update = cal.getTimeInMillis();
    }

    static void update(){
        try {
            List<HealthData> samsungstepsdata = Update.samsung.getAllStepsDataPoints(last_update);
            List<HealthData> samsungheartdata = Update.samsung.getAllHRDataPoints(last_update);
            update_stepcount(samsungstepsdata);
            update_hr(samsungheartdata);
            update_calorie(samsungstepsdata);
            update_speed(samsungstepsdata);
            update_distance(samsungstepsdata);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, 0);
            last_update = cal.getTimeInMillis();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void update_stepcount(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertstepsStoGdata(data);
            googledata.add(d);
        }
        googleFit.addStepCountData(googledata);
    }

    private static void update_distance(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertDistancetoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addDistanceData(googledata);
    }

    private static void update_speed(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertSpeedtoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addSpeedData(googledata);
    }

    private static void update_calorie(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertCalorietoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addCalorieData(googledata);
    }


    private static void update_hr(List<HealthData> samsungdata) {
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = Transform.convertHRStoGdatainst(data);
            googledata.add(d);
        }
        googleFit.addHeartRateData(googledata);
    }
}
