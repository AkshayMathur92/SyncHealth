package com.example.amathur.synchealth;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.fitness.data.DataPoint;
import com.samsung.android.sdk.healthdata.HealthData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends
        AppCompatActivity {

    public static final String APP_TAG = "SYNCHEALTH";
    private static MainActivity mInstance = null;
    private Button sync_button ;
    private static Samsung samsung;
    private static Google googleFit;
    public static Context mainContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainContext = this.getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;
        samsung = new Samsung(mInstance);
        googleFit = new Google(mInstance, savedInstanceState);

        sync_button = findViewById(R.id.sync_button);
        sync_button.setOnClickListener(v -> {
            update_stepcount();
            update_hr();
        });

        MySyncService.scheduleJob(this);
    }

    static void update_stepcount() {
        try {
            List<HealthData> samsungdata = samsung.getAllStepsDataPoints();
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

    @Override
    public void onDestroy() {
        samsung.disconnect();
        super.onDestroy();
    }
}