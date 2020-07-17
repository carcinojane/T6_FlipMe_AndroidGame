package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import android.widget.MediaController;

public class VideoActivity extends AppCompatActivity {

    protected VideoView videoView;
    protected MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN);
        playVideo();
    }

    protected void playVideo(){
        videoView = (VideoView) findViewById(R.id.videoView);
        if (videoView == null)
            return;

        String path="android.resource://"+getPackageName()+"/"+R.raw.demo_edit;
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);

        mc = new MediaController(this);
        videoView.setMediaController(mc);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mediaPlayer){
                videoView.start();
            }
        });
    }
}