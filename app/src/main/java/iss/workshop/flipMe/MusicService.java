package iss.workshop.flipMe;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

    MediaPlayer menuMusicPlayer;
    MediaPlayer gameMusicPlayer;
    MediaPlayer congratulationMusicPlayer;
    MediaPlayer timeoutMusicPlayer;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void playMenuSong() {
        if (gameMusicPlayer != null) gameMusicPlayer.pause();
        if (congratulationMusicPlayer != null) congratulationMusicPlayer.pause();
        if (timeoutMusicPlayer != null) timeoutMusicPlayer.pause();
        if (menuMusicPlayer == null) {
            menuMusicPlayer = MediaPlayer.create(this, R.raw.background);
            menuMusicPlayer.setLooping(true);
        }
        menuMusicPlayer.start();
    }

    public void playGameSong() {
        if (menuMusicPlayer != null) menuMusicPlayer.pause();
        if (congratulationMusicPlayer != null) congratulationMusicPlayer.pause();
        if (timeoutMusicPlayer != null) timeoutMusicPlayer.pause();
        if (gameMusicPlayer == null) {
            gameMusicPlayer = MediaPlayer.create(this, R.raw.game);
            gameMusicPlayer.setLooping(true);
        }
        gameMusicPlayer.start();
    }

    public void playCongratulationSong() {
        if (menuMusicPlayer != null) menuMusicPlayer.pause();
        if (gameMusicPlayer != null) gameMusicPlayer.pause();
        if (timeoutMusicPlayer != null) timeoutMusicPlayer.pause();
        if (congratulationMusicPlayer == null) {
            congratulationMusicPlayer = MediaPlayer.create(this, R.raw.congratulation);
            congratulationMusicPlayer.setLooping(false);
        }
        congratulationMusicPlayer.start();
    }

    public void playTimeOutSong() {
        if (menuMusicPlayer != null) menuMusicPlayer.pause();
        if (gameMusicPlayer != null) gameMusicPlayer.pause();
        if (congratulationMusicPlayer != null) congratulationMusicPlayer.pause();
        if (timeoutMusicPlayer == null) {
            timeoutMusicPlayer = MediaPlayer.create(this, R.raw.timeout);
            timeoutMusicPlayer.setLooping(false);
        }
        timeoutMusicPlayer.start();
    }

    public void stopPlaying() {
        if (menuMusicPlayer != null) menuMusicPlayer.pause();
        if (gameMusicPlayer != null) gameMusicPlayer.pause();
        if (congratulationMusicPlayer != null) congratulationMusicPlayer.pause();
        if (timeoutMusicPlayer != null) timeoutMusicPlayer.pause();
    }
}