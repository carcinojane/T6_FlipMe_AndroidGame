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



            //image adapter
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);

        }



    }


}