package com.example.amathur.synchealth;

import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.concurrent.TimeUnit;

class Transform {

    static DataPoint convertstepsStoGdata (HealthData data){
//        Log.d(APP_TAG, hd.getString("deviceuuid") + " steps = " + hd.getInt("count"));
        DataPoint dp =  DataPoint.create(Google.stepDataSource)
                .setTimeInterval(data.getLong("start_time"), data.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_STEPS).setInt(data.getInt("count"));
        return  dp;
    }

    static DataPoint convertHRStoGdatainst(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.heartrateDataSource)
                .setTimestamp(data.getLong("create_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_BPM).setFloat(data.getFloat("heart_rate"));
        return  dp;
    }

    static DataPoint convertCalorietoGdatainst(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.calorieDataSource)
                .setTimeInterval(data.getLong("start_time"), data.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_CALORIES).setFloat(data.getFloat("calorie"));
        return  dp;
    }

    static DataPoint convertDistancetoGdatainst(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.distanceDataSource)
                .setTimeInterval(data.getLong("start_time"), data.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_DISTANCE).setFloat(data.getFloat("distance"));
        return  dp;
    }

    static DataPoint convertSpeedtoGdatainst(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.speedDataSource)
                .setTimeInterval(data.getLong("start_time"), data.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_SPEED).setFloat(data.getFloat("speed"));
        return  dp;
    }
}
