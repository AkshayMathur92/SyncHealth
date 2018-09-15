package com.example.amathur.synchealth;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class MySyncService extends JobService{

    private static final String APP_TAG = "SYNCHEALTH-SYNCSERVICE";

    private static JobInfo jobInfo;
    final Runnable mWorker = () -> {
        Update.update_stepcount();
        Update.update_hr();
    };

    static {
        ComponentName serviceComponent = new ComponentName("com.example.amathur.synchealth", MySyncService.class.getName());
        jobInfo = new JobInfo.Builder(0, serviceComponent)
                .setPeriodic(1000 * 10)
                .build();
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(APP_TAG, "Sync Job has been scheduled");
        SyncThread.mHandler.post(mWorker);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void scheduleJob(Context context){
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(jobInfo);
    }
}
