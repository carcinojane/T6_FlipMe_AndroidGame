package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, GestureDetector.OnGestureListener, View.OnClickListener, ServiceConnection {

    MediaPlayer flip;
    MediaPlayer correct;
    MediaPlayer wrong;
    private MusicService musicService;
    GridView gridView;
    ArrayList<ImageDTO> allImages;
    ArrayList<ImageDTO> selectedImages = new ArrayList<>();
    ArrayList<Integer> selectedIds;
    ArrayList<Integer> matchedCards = new ArrayList<>();
    ImageView imageView1;
    ImageView imageView2;
    TextView matches;
    int selectCount = 0;
    int matchCount = 0;
    private ImageDTO selectid1;
    Handler handler;
    Runnable runnable;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    long remainder;
    CountDownTimer countDownTimer;
    int score = 0;
    EditText playerName;
    AlertDialog popUpBox;
    AlertDialog popUpBoxLose;
    int difficulty;
    String currentSong;
    boolean continuePlaying;
    private static final String TAG = "Swipe Position";
    private float y1;
    private GestureDetector gestureDetector;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //initialize gesture detector
        this.gestureDetector = new GestureDetector(GameActivity.this, this);

        //start timer
        startTimer();

        Intent intent1 = new Intent(this, MusicService.class);
        bindService(intent1, this, BIND_AUTO_CREATE);
        currentSong = "game";

        flip = MediaPlayer.create(this, R.raw.flip);
        wrong = MediaPlayer.create(this, R.raw.wrong);
        correct = MediaPlayer.create(this, R.raw.correct);

        //get selected images
        Intent intent = getIntent();
        selectedIds = intent.getIntegerArrayListExtra("ImageIds");
        assert selectedIds != null;
        Collections.shuffle(selectedIds);
        allImages = WebViewActivity.images;
        matches = findViewById(R.id.matchesTxt);

        //get difficulty
        difficulty = intent.getIntExtra("difficulty", 6);
        matches.setText(matchCount + "/" + difficulty);

        for (int selectedId : selectedIds) {
            for (ImageDTO image : allImages) {
                int id = image.getId();
                if (id == selectedId) {
                    ImageDTO sImage = new ImageDTO(image.getId(), image.getBitmap());
                    selectedImages.add(sImage);
                }
            }
        }

        //set image adapter to game gridView
        gridView = findViewById(R.id.gameGridview);

        if (difficulty == 10) {
            gridView.setNumColumns(4);
        }

        ImageAdapter imageAdapter = new ImageAdapter(this, selectedImages);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setVerticalScrollBarEnabled(false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                imageView1.setImageResource(R.drawable.dummy);
                imageView2.setImageResource(R.drawable.dummy);

            }
        };


    }

    @Override
    public void onPause() {
        super.onPause();
        if (musicService != null && !continuePlaying) musicService.stopPlaying();
    }

    void playMusic() {
        if (musicService != null) {
            switch (currentSong) {
                case "game":
                    musicService.playGameSong();
                    break;
                case "congratulation":
                    musicService.playCongratulationSong();
                    break;
                case "timeout":
                    musicService.playTimeOutSong();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }


    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.submitBtn) {

            pref = getSharedPreferences("players", MODE_PRIVATE);
            editor = pref.edit();

            int i = 0;
            while (pref.contains("name" + i)) {
                i++;
            }

            editor.putString("name" + i, playerName.getText().toString());
            editor.putInt("score" + i, score);
            editor.commit();

            popUpBox.dismiss();

            //route user to leader board listing
            Intent intent = new Intent(GameActivity.this, LeaderBoardActivity.class);
            intent.putExtra("currentSong", currentSong);
            continuePlaying = true;
            startActivity(intent);
        }

        if (id == R.id.okBtn) {
            popUpBoxLose.dismiss();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        currentSong = "game";
        ViewGroup gridElement = (ViewGroup) gridView.getChildAt(i);
        ImageView currImg = (ImageView) gridElement.getChildAt(0);
        ImageDTO image = selectedImages.get(i);
        currImg.setImageBitmap(image.getBitmap());
        flip.start();
        if (!matchedCards.contains(image.getId())) {
            if (matchCount < difficulty) {
                ImageDTO selectid2;
                if (selectCount == 0) {
                    selectid1 = null;
                    imageView1 = null;
                    imageView2 = null;
                }
                selectCount++;
                if (selectCount == 1) {
                    currImg.setImageBitmap(image.getBitmap());
                    selectid1 = image;
                    imageView1 = currImg;
                } else if (selectCount == 2) {
                    currImg.setImageBitmap(image.getBitmap());
                    selectid2 = image;
                    imageView2 = currImg;

                    if (selectid1 != null && imageView1 != null) {

                        if (selectid1.getBitmap() != selectid2.getBitmap() && imageView1 != null && imageView2 != null) {
                            handler.postDelayed(runnable, 300);
                            wrong.start();
                        } else if (selectid1.getBitmap() == selectid2.getBitmap()) {
                            matchCount++;
                            matchedCards.add(image.getId());
                            matches.setText(matchCount + "/" + difficulty);
                            correct.start();
                        }
                    }
                    selectCount = 0;
                }
            }
            if (matchCount == difficulty) {
                stopGame();
            }
        }
    }

    private void startTimer() {
        final TextView timer = findViewById(R.id.timer);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                int minutes = (int) millisUntilFinished / 60000;
                int seconds = (int) (millisUntilFinished) / 1000;
                timer.setText(String.format("%d:%02d", minutes, seconds));
                remainder = millisUntilFinished;
            }

            public void onFinish() {
                stopGame();

            }
        }.start();
    }

    private void totalScore() {
        if (difficulty == 6) {
            score = (int) remainder / 100;
        }
        if (difficulty == 10) {
            score = (int) remainder / 50;
        }
        if (remainder < 0) {
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
        musicService.playCongratulationSong();

        //display player's final score;
        TextView finalScore = popUp.findViewById(R.id.score);
        finalScore.setText(String.valueOf(score));

        //submit button
        final Button submitName = popUp.findViewById(R.id.submitBtn);
        if (submitName != null) {
            submitName.setOnClickListener(this);
            submitName.setEnabled(false);
        }

        //get player name;
        playerName = popUp.findViewById(R.id.nameTxt);
        playerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (playerName.getText().toString().length() == 0) {
                    assert submitName != null;
                    submitName.setEnabled(false);
                } else {
                    assert submitName != null;
                    submitName.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (playerName.getText().toString().length() == 0) {
                    assert submitName != null;
                    submitName.setEnabled(false);
                } else {
                    assert submitName != null;
                    submitName.setEnabled(true);
                }

            }
        });


        popUpBox.show();

    }

    public void popUpDialogueLose() {

        //build pop up dialogue;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View popUp = inflater.inflate(R.layout.pop_up_dialogue_box_lose, null);


        builder.setView(popUp);
        builder.setCancelable(false);
        popUpBoxLose = builder.create();

        //submit button
        Button okBtn = popUp.findViewById(R.id.okBtn);
        if (okBtn != null) {
            okBtn.setOnClickListener(this);
        }

        popUpBoxLose.show();

    }

    void stopGame() {
        countDownTimer.cancel();
        if (matchCount == difficulty) {
            totalScore();
            popUpDialogue();
        } else {
            popUpDialogueLose();
            musicService.playTimeOutSong();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
        if (binder != null) {
            musicService = binder.getService();
            playMusic();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    //override on touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        int MIN_DISTANCE = 150;
        switch (event.getAction()) {
            //starting to swipe time gesture
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            //ending time swipe gesture
            case MotionEvent.ACTION_UP:
                float y2 = event.getY();

                //getting value for vertical swipe
                float valueY = y2 - y1;

                if (Math.abs(valueY) > MIN_DISTANCE) {
                    //detect left to right swipe
                    if (y1 > y2) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
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
}
