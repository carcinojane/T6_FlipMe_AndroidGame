package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity
    implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        SharedPreferences pref = getSharedPreferences("players",MODE_PRIVATE);

        Button backBtn = findViewById(R.id.backBtn);
        if(backBtn!=null){
            backBtn.setOnClickListener(this);
        }

        //leaderBoardListing();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.backBtn){
            Intent intent = new Intent(LeaderBoardActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }




}