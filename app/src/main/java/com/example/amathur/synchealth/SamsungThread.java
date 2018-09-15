package com.example.amathur.synchealth;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

class SamsungThread extends Thread {
    public static Handler mHandler;
    public void run() {
        this.setName("SamsungThread");
        Looper.prepare();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                Log.d("SAMSUNGTHREAD", msg.toString());
            }
        };
        Looper.loop();
    }
}