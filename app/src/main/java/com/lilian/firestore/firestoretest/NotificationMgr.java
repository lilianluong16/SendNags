package com.lilian.firestore.firestoretest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationMgr {
    Context mContext;
    public AlarmManager alarmMgr;
    public ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

    public NotificationMgr(Context ctxt){
        mContext = ctxt;
        alarmMgr = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);

        /*
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("message", "hi");
        intent.putExtra("sender", "hithere");
        // int requestCode = intentArray.size();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 4, intent, 0);
       alarmMgr.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 3000,
                pendingIntent);
        */

    }

    public void addAlarm(String msg, String sender, int hour, int minute, int requestCode){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        while(System.currentTimeMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("message", msg);
        intent.putExtra("sender", sender);
        // int requestCode = intentArray.size();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, 0);
        Log.d("test", String.valueOf(calendar.getTimeInMillis()));
        Log.d("test", String.valueOf(System.currentTimeMillis()));
        Log.d("this is alarm set for", String.valueOf(hour) + ":" + String.valueOf(minute));
        Log.d("setting alarm now", String.valueOf(calendar.getTimeInMillis() - System.currentTimeMillis()) + " remaining");
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public void cancelAlarm(String msg, String sender, int hour, int minute, int requestCode){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("message", msg);
        intent.putExtra("sender", sender);
        // int requestCode = intentArray.size();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, 0);
        alarmMgr.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public boolean checkAlarm(String msg, String sender, int hour, int minute, int requestCode){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("message", msg);
        intent.putExtra("sender", sender);
        Log.d("Notifier", String.valueOf(calendar.getTimeInMillis()));
        Log.d("Notifier", String.valueOf(System.currentTimeMillis()));
        // int requestCode = intentArray.size();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

}
