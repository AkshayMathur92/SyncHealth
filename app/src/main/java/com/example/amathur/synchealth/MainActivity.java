package com.example.amathur.synchealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class MainActivity
        extends
        AppCompatActivity {

    public static final String APP_TAG = "SYNCHEALTH";

    static {
        LocalThreadPool.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Update.samsung = new Samsung(this);
        Update.googleFit = new Google(this, savedInstanceState);

        Button sync_button = findViewById(R.id.sync_button);
        sync_button.setOnClickListener(v -> LocalThreadPool.syncHandler.post(Update::update));

        MySyncService.scheduleJob(this);
    }

    @Override
    public void onDestroy() {
        Log.d(APP_TAG,"onDestroy Connection Closed ");
        Update.samsung.disconnect();
        super.onDestroy();
    }
}