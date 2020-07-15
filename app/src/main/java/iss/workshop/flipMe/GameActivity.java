package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    GridView gridView;
    ImageView imageView;
    ArrayList<ImageDAO> selectedImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        selectedImages = new ArrayList<>();
        for(int i=0; i<=6 ;i++){
            selectedImages.add(new ImageDAO(null,null));
        }
        gridView = (GridView)findViewById(R.id.gameGridView);
        imageView=(ImageView)findViewById(R.id.imageview);
        ImageAdapter imageAdapter = new ImageAdapter(this,selectedImages);
        gridView.setAdapter(imageAdapter);
        gridView.setVerticalScrollBarEnabled(false);
    }
}