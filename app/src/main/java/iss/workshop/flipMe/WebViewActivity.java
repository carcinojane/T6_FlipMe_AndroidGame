package iss.workshop.flipMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class WebViewActivity extends AppCompatActivity
        implements View.OnClickListener, FetchAsyncTask.ICallback, GestureDetector.OnGestureListener, AdapterView.OnItemClickListener {

    //declare variables
    public static final int NO_OF_IMAGES = 20;
    public static ArrayList<ImageDTO> images;
    private ArrayList<ImageDTO> allImages;
    private String url;
    private ProgressBar progressBar;
    private TextView progressTxt;
    public int pos=0;
    FetchAsyncTask fetchTask;
    int imageCount =0;
    int difficulty;

    private static final String TAG = "Swipe Position";
    private float y1;
    private GestureDetector gestureDetector;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private boolean isFetchButtonClicked=false;

    //UI Elements
    EditText urlTxt;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent= getIntent();
        difficulty = intent.getIntExtra("difficulty",6);
        System.out.println(intent.getStringExtra("test"));

        //initialize gesture detector
        this.gestureDetector = new GestureDetector(WebViewActivity.this, this);

        //instantiate variables
        images = new ArrayList<>();
        for(int i=0; i<NO_OF_IMAGES ;i++){
            images.add(new ImageDTO(null,null));
        }
        allImages= new ArrayList<>();


        //get UI Elements
        Button btnFetch = findViewById(R.id.btnFetch);
        progressTxt= findViewById(R.id.progressTxt);
        urlTxt= findViewById(R.id.urlTxt);
        gridView = findViewById(R.id.gridview);
        selectedIds.clear();

        //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressTxt=findViewById(R.id.progressTxt);
        progressBar.setMax(20);


        ImageAdapter imageAdapter = new ImageAdapter(this,images);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setVerticalScrollBarEnabled(false);

        //set onCLickListeners
        if(btnFetch!=null){
            btnFetch.setOnClickListener(this);
        }

        urlTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        isFetchButtonClicked=true;
        int id = view.getId();
        if(id==R.id.btnFetch){
            url = urlTxt.getText().toString();
            clearImages();
            startFetchTask();
        }

        if(id==R.id.urlTxt){
            if(fetchTask != null){
                fetchTask.cancel(true);
            }
        }

    }

    void startFetchTask(){
        if(fetchTask != null){
            fetchTask.cancel(true);
        }
        fetchTask = new FetchAsyncTask(this);
        fetchTask.execute(url);
    }

    @Override
    public void AddImages(ImageDTO image) {
        if(imageCount<images.size()) {
            Message msg = new Message();
            msg.obj = image;
            images.get(imageCount).setBitmap(image.getBitmap());
            images.get(imageCount).setId(imageCount);
            allImages.add(image);
            imageCount++;
            mainHandler.sendMessage(msg);
        }
    }

    public void clearImages(){
        allImages.clear();
        imageCount=0;
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
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
      @SuppressLint("SetTextI18n")
      @Override
      public void handleMessage(@NonNull Message msg){
          progressBar.incrementProgressBy(1);
          progressTxt.setText("downloading "+progressBar.getProgress()+"/"+ progressBar.getMax());

          if(progressBar.getProgress()==progressBar.getMax()){
              progressBar.setVisibility(View.INVISIBLE);
              progressTxt.setText("Pick "+difficulty+" images");
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
        intent.putIntegerArrayListExtra("ImageIds",selectedIds);
        intent.putExtra("difficulty",difficulty);
        startActivity(intent);
    }

    //override on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        int MIN_DISTANCE = 150;
        switch (event.getAction()){
            //starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            //ending time swipe gesture
            case MotionEvent.ACTION_UP:
                float y2 = event.getY();

                //getting value for vertical swipe
                float valueY = y2 - y1;

                if(Math.abs(valueY)> MIN_DISTANCE){
                    //detect left to right swipe
                    if (y1> y2){
                        super.finish();
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_in_bottom);
                        Log.d(TAG, "Top Swipe");
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ImageView imageView= view.findViewById(R.id.imageview);
        ImageDTO image = images.get(i);
        int imageId=image.getId();
        System.out.println(imageId);
        if(isFetchButtonClicked){
        if(selectedIds.contains(imageId)){
            selectedIds.remove(Integer.valueOf(imageId));
            selectedIds.remove(Integer.valueOf(imageId));
            imageView.clearColorFilter();
        }
        else{
            selectedIds.add(imageId);
            selectedIds.add(imageId);
            imageView.setColorFilter(R.color.LavenderWeb);
            if(selectedIds.size()==difficulty*2){
                startGameActivity();
            }
        }
        progressTxt.setText(selectedIds.size()/2+"/"+difficulty+" images selected");

        }

    }
}
