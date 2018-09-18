package com.example.amathur.synchealth;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.Objects;

public class MySyncService extends JobService{

    private static final String APP_TAG = "SYNCHEALTH-SYNCSERVICE";

    private static final JobInfo jobInfo;
    private final Runnable mWorker;

    static {
        ComponentName serviceComponent = new ComponentName("com.example.amathur.synchealth", MySyncService.class.getName());
        jobInfo = new JobInfo.Builder(0, serviceComponent)
                .setPeriodic(1000 * 10)
                .build();
    }

    public MySyncService() {
        mWorker = Update::update;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(APP_TAG, "Sync Job has been scheduled");
        LocalThreadPool.syncHandler.post(mWorker);
        jobFinished(params, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public static void scheduleJob(Context context){
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        Objects.requireNonNull(jobScheduler).schedule(jobInfo);
    }
}
