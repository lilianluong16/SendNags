package com.lilian.firestore.firestoretest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Alarm_Receiver extends BroadcastReceiver {
    private NotificationHelper mNotificationHelper;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver", "Yay!");

//create intent for ringtone
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //start
        context.startService(service_intent);
        mNotificationHelper = new NotificationHelper(context);
        sendOnChannel1(MainActivity.title, MainActivity.message);

    }
    public void sendOnChannel1(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title, message);
        mNotificationHelper.getManager().notify(1, nb.build());
    }
}
