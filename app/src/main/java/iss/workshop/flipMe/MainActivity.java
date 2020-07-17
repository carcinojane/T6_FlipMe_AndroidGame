package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        Button btnStart = findViewById(R.id.btnStart);


        //set onClickListener
        if(btnStart!=null){
            btnStart.setOnClickListener(this);
        }

        Button btnBoard = findViewById(R.id.btnLBoard);
        if(btnBoard!=null){
            btnBoard.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btnStart) {
            //get images bitmap

            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_top,R.anim.slide_in_top);
        }

        if(id==R.id.btnLBoard){
            Intent intent = new Intent(this, LeaderBoardActivity.class);
            startActivity(intent);
        }

    }

}