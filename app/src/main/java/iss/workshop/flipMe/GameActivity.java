package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    GridView gridView;
    ImageView imageView;
    ArrayList<ImageDTO> allImages;
    ArrayList<ImageDTO> selectedImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        //get selected images
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        selectedImages=(ArrayList<ImageDTO>)args.getSerializable("selected");

        //set image adapter to game gridView
        gridView = (GridView)findViewById(R.id.gameGridview);
        imageView=(ImageView)findViewById(R.id.gameImageview);
        ImageAdapter imageAdapter = new ImageAdapter(this,selectedImages);
        gridView.setAdapter(imageAdapter);
        gridView.setVerticalScrollBarEnabled(false);
    }
}