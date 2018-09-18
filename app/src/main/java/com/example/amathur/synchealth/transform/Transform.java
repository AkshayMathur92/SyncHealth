package com.example.amathur.synchealth.transform;

import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;

public interface Transform {
    DataPoint transform (HealthData data);
}
