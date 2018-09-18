package com.example.amathur.synchealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Button;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity
        extends
        AppCompatActivity {

    public static final String APP_TAG = "SYNCHEALTH";

    static {
        LocalThreadPool.start();
    }

    public interface SyncListener{
        void onSync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Update.syncListener = (() -> runOnUiThread(() -> ((TextView)findViewById(R.id.LastLog)).setText(convertTime(Update.last_update))));
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

    @Override
    public void onResume(){
        Log.d(APP_TAG,"onResume");
        updateTime();
        super.onResume();
    }

    public void updateTime(){
        ((TextView)findViewById(R.id.LastLog)).setText(convertTime(Update.last_update));
    }
    private static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}