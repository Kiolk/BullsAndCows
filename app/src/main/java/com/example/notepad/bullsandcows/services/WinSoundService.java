package com.example.notepad.bullsandcows.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.notepad.bullsandcows.R;

public class WinSoundService extends Service {

    MediaPlayer mMediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent pIntent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("sound", "start");
        mMediaPlayer = MediaPlayer.create(WinSoundService.this, R.raw.bull);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(false);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        super.onDestroy();
    }
}
