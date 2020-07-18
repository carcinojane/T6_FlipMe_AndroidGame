package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.MediaController;

public class VideoActivity extends AppCompatActivity
    implements View.OnClickListener{

    protected VideoView videoView;
    protected MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN);
        playVideo();

        Button backBtn = findViewById(R.id.btnBack);
        if(backBtn!=null){
            backBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        if (id == R.id.btnBack){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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