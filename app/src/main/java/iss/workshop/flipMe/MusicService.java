package iss.workshop.flipMe;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer = null;

    private boolean isReady = false;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.background);
        if(mediaPlayer == null){
            return;
        }

        mediaPlayer.stop();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.release();
                stopSelf();
                return false;
            }
        });

        try{
            mediaPlayer.prepare();
            isReady = true;
        } catch (IOException e) {
            e.printStackTrace();
            isReady = false;
        }

        if(isReady){
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(isReady && !mediaPlayer.isPlaying()){
            //播放背景音乐
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }
}