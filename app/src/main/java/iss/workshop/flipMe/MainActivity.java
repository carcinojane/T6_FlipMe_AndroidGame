package iss.workshop.flipMe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
    EditText playerName;
    AlertDialog popUpBox;

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

        if(id==R.id.submitBtn){
            SharedPreferences pref =getSharedPreferences("players", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("name", playerName.toString());
            editor.putInt("score", score);
            editor.commit();
            popUpBox.dismiss();

            //route user to leader board listing
            Intent intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
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

    public void popUpDialogue() {

        //build pop up dialogue;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View popUp = inflater.inflate(R.layout.pop_up_dialogue_box, null);

        builder.setView(popUp);
        builder.setCancelable(false);
        popUpBox = builder.create();

        //display header
        TextView header = popUp.findViewById(R.id.header);
        header.setText(R.string.game_completed);

        //display player's final score;
        TextView finalScore = findViewById(R.id.score);
        finalScore.setText(String.valueOf(score));

        //submit button
        Button submitName = popUp.findViewById(R.id.submitBtn);
        if (submitName != null) {
            submitName.setOnClickListener(this);
            submitName.setEnabled(false);
        }

        //get player name;
        playerName = popUp.findViewById(R.id.nameTxt);
        if(playerName.getText().length()!=0){
            submitName.setEnabled(true);
        }


        popUpBox.show();
    }
}