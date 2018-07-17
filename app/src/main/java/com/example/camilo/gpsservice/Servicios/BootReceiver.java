package com.example.camilo.gpsservice.Servicios;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.camilo.gpsservice.R;

public class BootReceiver extends BroadcastReceiver {

    private static final int PERIOD_MS = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.boot_message, Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            scheduleIntent(context);
        } else {
            scheduleJob(context);
        }
    }

    public static void scheduleIntent(Context context){
        Intent newIntent = new Intent(context, BackgroundIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, newIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), PERIOD_MS,
                pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, BackgroundJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(PERIOD_MS);
        builder.setOverrideDeadline(PERIOD_MS);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}