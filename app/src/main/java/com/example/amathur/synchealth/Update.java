package com.example.amathur.synchealth;

import android.appwidget.AppWidgetProvider;
import android.util.Log;

import com.example.amathur.synchealth.transform.CalorieTransform;
import com.example.amathur.synchealth.transform.DistanceTransform;
import com.example.amathur.synchealth.transform.HeartRateTransform;
import com.example.amathur.synchealth.transform.SpeedTransform;
import com.example.amathur.synchealth.transform.StepsTransform;
import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;
import com.example.amathur.synchealth.transform.Transform;

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
            Log.d("SYNCHEALTHUpdate", "last_update_time = " + last_update);
            List<HealthData> samsungstepsdata = Update.samsung.getAllStepsDataPoints(last_update);
            List<HealthData> samsungheartdata = Update.samsung.getAllHRDataPoints(last_update);

            googleFit.addStepCountData(convert( samsungstepsdata, new StepsTransform()));
            googleFit.addCalorieData(convert( samsungstepsdata, new CalorieTransform()));
            googleFit.addSpeedData(convert( samsungstepsdata, new SpeedTransform()));
            googleFit.addDistanceData(convert( samsungstepsdata, new DistanceTransform()));
            googleFit.addHeartRateData(convert( samsungheartdata, new HeartRateTransform()));

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MINUTE, 0);
            last_update = cal.getTimeInMillis();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<DataPoint> convert(List<HealthData> samsungdata, Transform t){
        List<DataPoint> googledata = new ArrayList<>();
        for(HealthData data: samsungdata){
            DataPoint d = t.transform(data);
            googledata.add(d);
        }
        return googledata;
    }
}
