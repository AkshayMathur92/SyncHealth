package com.example.amathur.synchealth;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

class LocalThreadPool {
    public static Handler syncHandler;
    public static Handler callbackHandler;

    private static final Thread syncThread;

    private static final Thread callbackThread;

    static {
        syncThread = new Thread() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                Looper.prepare();
                syncHandler = new Handler();
                Looper.loop();
            }
        };
        syncThread.setName("syncThread");

        callbackThread = new Thread() {
            @SuppressLint("HandlerLeak")
            @Override
            public void run() {
                Looper.prepare();
                callbackHandler = new Handler();
                Looper.loop();
            }
        };
        callbackThread.setName("callbackThread");
    }

    static void start(){
        syncThread.start();
        callbackThread.start();
    }
}