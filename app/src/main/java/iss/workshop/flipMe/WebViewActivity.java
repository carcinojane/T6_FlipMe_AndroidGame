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

    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private boolean isFetchButtonClicked=false;

    //UI Elements
    EditText urlTxt;
    GridView gridView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //initialize gesture detector
        this.gestureDetector = new GestureDetector(WebViewActivity.this, this);

        //instantiate variables
        images = new ArrayList<>();
        for(int i=0; i<NO_OF_IMAGES ;i++){
            images.add(new ImageDTO(null,null));
        }
        allImages= new ArrayList<>();


        //get UI Elements
        Button btnFetch = (Button)findViewById(R.id.btnFetch);
        progressTxt=(TextView) findViewById(R.id.progressTxt);
        urlTxt= (EditText)findViewById(R.id.urlTxt);
        gridView = (GridView)findViewById(R.id.gridview);
        //imageView=(ImageView)findViewById(R.id.imageview);
        selectedIds.clear();

        //progress bar
        progressBar = findViewById(R.id.progressBar);
        progressTxt=findViewById(R.id.progressTxt);
        //progressBar.setMax(100);
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
        //mainHandler.removeCallbacksAndMessages(null);
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
            progressBar.incrementProgressBy(1);
            progressTxt.setText("downloading "+progressBar.getProgress()+"/"+ progressBar.getMax());

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
        intent.putIntegerArrayListExtra("ImageIds",selectedIds);
        startActivity(intent);
    }

    //override on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            //starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            //ending time swipe gesture
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                //getting value for horizontal swipe
                float valueX = x2 - x1;

                //getting value for vertical swipe
                float valueY = y2 - y1;

                if(Math.abs(valueY)>MIN_DISTANCE){
                    //detect left to right swipe
                    if (y1>y2){
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ViewGroup gridElement = (ViewGroup) gridView.getChildAt(i);
        ImageView currImg = (ImageView) gridElement.getChildAt(0);
        int imageId=currImg.getId();
        System.out.println(imageId);
        if(isFetchButtonClicked){
            if(selectedIds.contains(imageId)){
                selectedIds.remove(new Integer(imageId));
                selectedIds.remove(new Integer(imageId));
                currImg.clearColorFilter();;
            }
            else{
                selectedIds.add(imageId);
                selectedIds.add(imageId);
                currImg.setColorFilter(R.color.MintCream);
                if(selectedIds.size()==12){
                    startGameActivity();
                }
            }
            progressTxt.setText(selectedIds.size()/2+"/6 images selected");

        }

    }
}