package com.example.tamagochi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.credentials.playservices.CredentialProviderMetadataHolder;

public class MediaPlayback extends Service {
    private final IBinder binder  =  new LocalBinder();
    public class LocalBinder extends Binder{
        public MediaPlayback getService()
        {
            return MediaPlayback.this;
        }
    }

    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_sound);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    public void startMusic()
    {
        if(mediaPlayer != null && !mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }

    public void stopMusic()
    {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
