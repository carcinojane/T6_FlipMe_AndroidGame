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
    ArrayList<ImageDTO> selectedImages = new ArrayList<>();
    ArrayList<Integer> selectedIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get selected images
        Intent intent = getIntent();
        selectedIds= intent.getIntegerArrayListExtra("ImageIds");
        allImages=WebViewActivity.images;

        for(int selectedId:selectedIds){
            for(ImageDTO image:allImages){
                int id = image.getId();
                if(id==selectedId){
                    selectedImages.add(image);
                }
            }
        }

        //set image adapter to game gridView
        gridView = (GridView)findViewById(R.id.gameGridview);
        imageView=(ImageView)findViewById(R.id.gameImageview);
        ImageAdapter imageAdapter = new ImageAdapter(this,selectedImages);
        gridView.setAdapter(imageAdapter);
        gridView.setVerticalScrollBarEnabled(false);
    }
}