package com.example.amathur.synchealth;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

class SyncThread extends Thread {
    public static Handler mHandler;
    public void run() {
        this.setName("SyncThread");
        Looper.prepare();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d("SYNCTHREAD", msg.toString());
            }
        };
        Looper.loop();
    }
}