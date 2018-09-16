package com.lilian.firestore.firestoretest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class RingtonePlayer extends Service {


    MediaPlayer media_song;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {


        //sounds
        Log.d("ringtone", "play");
        media_song = MediaPlayer.create(this, R.raw.sound);
        media_song.start();


        return START_NOT_STICKY;
    };



}
