package com.example.amathur.synchealth.transform;

import com.example.amathur.synchealth.Google;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.concurrent.TimeUnit;

public class SpeedTransform implements Transform{
    @Override
    public DataPoint transform(HealthData data) {
        DataPoint dp =  DataPoint.create(Google.speedDataSource)
                .setTimeInterval(data.getLong("start_time"), data.getLong("end_time"), TimeUnit.MILLISECONDS);
        dp.getValue(Field.FIELD_SPEED).setFloat(data.getFloat("speed"));
        return  dp;
    }
}
