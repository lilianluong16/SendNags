package com.lilian.firestore.firestoretest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    private NotificationHelper mNotificationHelper;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("We are in the receiver", "Yay!");

//create intent for ringtone
        Intent service_intent = new Intent(context, RingtonePlayer.class);

        //start
        context.startService(service_intent);
        mNotificationHelper = new NotificationHelper(context);
        String message = intent.getExtras().get("message").toString();
        String sender = intent.getExtras().get("sender").toString();
        sendOnChannel1(message, sender + " reminds you to " + message);

    }
    public void sendOnChannel1(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title, message);
        mNotificationHelper.getManager().notify(1, nb.build());
    }
}
/*
public class AlarmReceiver extends BroadcastReceiver {

    Context mContext;
    private NotificationManager mManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent){
        Log.e("We are in the receiver", "Yay!");

        Intent service_intent = new Intent(context, RingtonePlayer.class);
        context.startService(service_intent);
        String message = intent.getExtras().get("message").toString();
        String sender = intent.getExtras().get("sender").toString();

        Intent next = new Intent(context, NavActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(next);
        PendingIntent next_pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("RECEIVER", android.os.Build.VERSION.SDK_INT + "");
        if(android.os.Build.VERSION.SDK_INT < 26){
            mBuilder = new NotificationCompat.Builder(context,"")
                    .setSmallIcon(R.drawable.ic_menu_send)
                    .setContentTitle(message)
                    .setContentText(sender + " reminds you to " + message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(next_pending)
                    .setAutoCancel(true);

        }else{
            NotificationChannel channel = new NotificationChannel("channelID", "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            mManager = context.getSystemService(NotificationManager.class);
            mManager.createNotificationChannel(channel);

            mBuilder = new NotificationCompat.Builder(context,"")
                    .setSmallIcon(R.drawable.ic_menu_send)
                    .setContentTitle(message)
                    .setContentText(sender + " reminds you to " + message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(next_pending)
                    .setAutoCancel(true);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, mBuilder.build());

    }

}
*/