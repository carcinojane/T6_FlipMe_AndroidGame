package iss.workshop.flipMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class WebViewActivity extends AppCompatActivity
        implements View.OnClickListener, FetchAsyncTask.ICallback {

    //declare variables
    public static final int NO_OF_IMAGES = 20;
    private ArrayList<ImageDTO> images;
    private ArrayList<ImageDTO> allImages;
    private String url;
    private ProgressBar progressBar;
    private TextView progressTxt;
    public int pos=0;
    FetchAsyncTask fetchTask;


    //UI Elements
    EditText urlTxt;
    GridView gridView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //instantiate variables
        images = new ArrayList<>();
        for(int i=0; i<=NO_OF_IMAGES ;i++){
            images.add(new ImageDTO(null,null));
        }
        allImages= new ArrayList<>();


        //get UI Elements
        Button btnFetch = (Button)findViewById(R.id.btnFetch);
        progressTxt=(TextView) findViewById(R.id.progressTxt);
        urlTxt= (EditText)findViewById(R.id.urlTxt);
        gridView = (GridView)findViewById(R.id.gridview);
        imageView=(ImageView)findViewById(R.id.imageview);

        //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressTxt=findViewById(R.id.progressTxt);
        progressBar.setMax(100);


        ImageAdapter imageAdapter = new ImageAdapter(this,images);
        gridView.setAdapter(imageAdapter);
        gridView.setVerticalScrollBarEnabled(false);

        //set onCLickListeners
        if(btnFetch!=null){
            btnFetch.setOnClickListener(this);
        }

        urlTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.btnFetch){
            url = urlTxt.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            startFetchTask();
        }

        if(id==R.id.urlTxt){
            fetchTask.cancel(true);
        }

    }

    void startFetchTask(){
        if(fetchTask != null){
            fetchTask.cancel(true);
        }
        fetchTask = new FetchAsyncTask(this);
        fetchTask.execute(url);
    }

    public ArrayList<ImageDTO> getAllImages(){
        return allImages;
    }


    @Override
    public void AddImages(ImageDTO image) {
        if(allImages.size()<NO_OF_IMAGES){
            Message msg = new Message();
            msg.obj= image;
            allImages.add(image);
            mainHandler.sendMessage(msg);
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mainHandler= new Handler(){
        public void handleMessage(@NonNull Message msg){
            ImageDTO image = (ImageDTO)msg.obj;
            ViewGroup gridElement = (ViewGroup) gridView.getChildAt(pos);
            ImageView currImg = (ImageView) gridElement.getChildAt(0);
            currImg.setImageBitmap(image.getBitmap());
            pos++;
            System.out.println(pos);
            if(pos>=NO_OF_IMAGES){
            pos=0;
            }
            progressBarHandler.sendMessage(progressBarHandler.obtainMessage());
        }
    };

    @SuppressLint("HandlerLeak")
    Handler progressBarHandler = new Handler(){
      @Override
      public void handleMessage(@NonNull Message msg){
          progressBar.incrementProgressBy(5);
          progressTxt.setText(progressBar.getProgress()+"%");

          if(progressBar.getProgress()==progressBar.getMax()){
              progressBar.setVisibility(View.INVISIBLE);
              progressTxt.setText("Pick 6 images");
          }
      }
    };


    @Override
    public void makeToast(String message) {
        final String innerMsg = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), innerMsg, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void startGameActivity(){
        Intent intent= new Intent(this,GameActivity.class);
        startActivity(intent);
    }
}