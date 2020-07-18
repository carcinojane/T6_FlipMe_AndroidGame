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
implements View.OnClickListener {
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

    }


    @Override
    public void onClick(View view) {
        Intent intent1 = new Intent(this, MusicService.class);
        startService(intent1);
        int id = view.getId();

        if(id==R.id.btnStartEasy) {
            //get images bitmap

            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("difficulty",6);
            intent.putExtra("test","hello");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
        }

        if(id==R.id.btnStartDifficult) {
            //get images bitmap

            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("difficulty",10);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
        }

        if(id==R.id.btnLBoard){
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            startActivity(intent);
        }
    }
}