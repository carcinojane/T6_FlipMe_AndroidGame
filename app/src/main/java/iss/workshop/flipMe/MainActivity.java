package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener {
    String url;
    ArrayList<ImageDAO> images;
    int secs = 0;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get UI elements
        Button btnStart = findViewById(R.id.btnStart);


        //set onClickListener
        if(btnStart!=null){
            btnStart.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btnStart) {
            //get images bitmap
            //start timer
            startTimer();



            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);

        }
    }

    private void startTimer(){
        final TextView timer = findViewById(R.id.timer);
        final Handler timerHandler = new Handler();
        timerHandler.post(new Runnable() {
            @Override
            public void run() {
                int minutes =(secs % 3600)/60;
                int seconds =secs % 60;
                timer.setText(String.format("%d:%2d", minutes,seconds));
                secs++;
                timerHandler.postDelayed(this, 500);
            }
        });
    }

    private void totalScore(){
        score = (60 - secs) * 10;
        if (score < 0){
            score = 0;
        }
    }
}