package com.example.amathur.synchealth;

import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.ArrayList;
import java.util.List;

public class Update {

    static Samsung samsung;
    static Google googleFit;

    static void update_stepcount() {
        try {
            List<HealthData> samsungdata = Update.samsung.getAllStepsDataPoints();
            List<DataPoint> googledata = new ArrayList<>();
            for(HealthData data: samsungdata){
                DataPoint d = Transform.convertstepsStoGdata(data);
                googledata.add(d);
            }
            googleFit.addStepCountData(googledata);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static void update_hr() {
        try {
            List<HealthData> samsungdata = samsung.getAllHRDataPoints();
            List<DataPoint> googledata = new ArrayList<>();
            for(HealthData data: samsungdata){
                DataPoint d = Transform.convertHRStoGdatainst(data);
                googledata.add(d);
            }
            googleFit.addHeartRateData(googledata);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
