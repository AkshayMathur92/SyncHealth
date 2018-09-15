package com.example.amathur.synchealth;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.concurrent.TimeUnit;

public class Transform {

    static DataPoint convertstepsStoGdata (HealthData hd){
//        Log.d(APP_TAG, hd.getString("deviceuuid") + " steps = " + hd.getInt("count"));
        DataPoint dp =  DataPoint.create(Google.stepdataSource)
                .setTimeInterval(hd.getLong("start_time"), hd.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_STEPS).setInt(hd.getInt("count"));
        return  dp;
    }

    static DataPoint convertHRStoGdatainst(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.heartrateDataSource)
                .setTimestamp(data.getLong("create_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_BPM).setFloat(data.getFloat("heart_rate"));
        return  dp;
    }
}
