package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener, ServiceConnection {
    MusicService musicService;
    boolean continuePlaying;
    String url;
    ArrayList<ImageDTO> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get UI elements
        Button btnStartEasy = findViewById(R.id.btnStartEasy);
        Button btnStartDifficult = findViewById(R.id.btnStartDifficult);

        //set onClickListener
        if(btnStartEasy!=null){
            btnStartEasy.setOnClickListener(this);
        }

        if(btnStartDifficult!=null){
            btnStartDifficult.setOnClickListener(this);
        }

        Button btnBoard = findViewById(R.id.btnLBoard);
        if(btnBoard!=null){
            btnBoard.setOnClickListener(this);
        }

        Button btnAbout = findViewById(R.id.btnAbout);
        if(btnAbout!=null){
            btnAbout.setOnClickListener(this);
        }

        Button btnVid = findViewById(R.id.btnVid);
        if(btnVid!=null){
            btnVid.setOnClickListener(this);
        }

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);

    }

    @Override
    public void onResume(){
        super.onResume();
        continuePlaying = false;
        if(musicService != null) musicService.playMenuSong();
    }

    @Override
    public void onPause(){
        super.onPause();
        if(musicService != null && !continuePlaying) musicService.stopPlaying();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.btnStartEasy) {
            continuePlaying=true;
  

            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("difficulty",6);
            intent.putExtra("test","hello");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
        }

        if(id==R.id.btnStartDifficult) {
            continuePlaying=true;


            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("difficulty",10);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
        }

        if(id==R.id.btnLBoard){
            continuePlaying=true;
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            startActivity(intent);
        }

        if(id==R.id.btnAbout){
            continuePlaying=true;
            Intent intent = new Intent(this,OnBoardActivity.class);
            startActivity(intent);
        }

        if(id==R.id.btnVid){
            continuePlaying=true;
            Intent intent = new Intent(this,VideoActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
        if(binder != null) {
            musicService = binder.getService();
            musicService.playMenuSong();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
