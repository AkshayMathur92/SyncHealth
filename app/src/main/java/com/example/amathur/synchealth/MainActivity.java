package com.example.amathur.synchealth;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity
        extends
        AppCompatActivity {

    public static final String APP_TAG = "SYNCHEALTH";
    private static MainActivity mInstance = null;
    private Button sync_button ;
    public static Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainContext = this.getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInstance = this;
        Update.samsung = new Samsung(mInstance);
        Update.googleFit = new Google(mInstance, savedInstanceState);

        sync_button = findViewById(R.id.sync_button);
        sync_button.setOnClickListener(v -> {
            Update.update_stepcount();
            Update.update_hr();
        });

        MySyncService.scheduleJob(this);
    }

    @Override
    public void onDestroy() {
        Update.samsung.disconnect();
        super.onDestroy();
    }
}