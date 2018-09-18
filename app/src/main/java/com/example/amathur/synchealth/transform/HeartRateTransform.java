package com.example.amathur.synchealth.transform;

import com.example.amathur.synchealth.Google;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.concurrent.TimeUnit;

public class HeartRateTransform implements Transform{
    @Override
    public DataPoint transform(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.heartrateDataSource)
                .setTimestamp(data.getLong("create_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_BPM).setFloat(data.getFloat("heart_rate"));
        return  dp;
    }
}
