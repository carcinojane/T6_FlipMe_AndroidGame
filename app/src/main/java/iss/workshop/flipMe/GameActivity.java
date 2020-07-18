package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
        implements AdapterView.OnItemClickListener, View.OnClickListener{

    private GameMusic gameMusic;
    GridView gridView;
    ImageView imageView;
    ArrayList<ImageDTO> allImages;
    ArrayList<ImageDTO> selectedImages = new ArrayList<>();
    ArrayList<Integer> selectedIds;
    ArrayList<Integer> matchedCards = new ArrayList<>();
    ImageView imageView1;
    ImageView imageView2;
    TextView matches;
    int selectCount=0;
    int matchCount=0;
    private ImageDTO selectid1;
    private ImageDTO selectid2;
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
    public static MediaPlayer game;
    int difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //start timer
        startTimer();

        game=MediaPlayer.create(this,R.raw.game);
        gameMusic=new GameMusic(this);



        //get selected images
        Intent intent = getIntent();
        selectedIds= intent.getIntegerArrayListExtra("ImageIds");
        Collections.shuffle(selectedIds);
        allImages=WebViewActivity.images;
        matches = (TextView)findViewById(R.id.matchesTxt);

        //get difficulty
        difficulty = intent.getIntExtra("difficulty",6);

        for(int selectedId:selectedIds){
            for(ImageDTO image:allImages){
                int id = image.getId();
                ImageDTO image1 = image;
                if(id==selectedId){
                    ImageDTO sImage = new ImageDTO(image1.getId(),image1.getBitmap());
                    selectedImages.add(sImage);
                }
            }
        }

        //set image adapter to game gridView
        gridView = (GridView)findViewById(R.id.gameGridview);

        if(difficulty==10){
            gridView.setNumColumns(4);
        }

        ImageAdapter imageAdapter = new ImageAdapter(this,selectedImages);
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
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.submitBtn) {

            pref = getSharedPreferences("players", MODE_PRIVATE);
            editor = pref.edit();

            int i = 0;
            while (true) {
                if (!pref.contains("name" + i)) {
                    break;
                }
                i++;
            }

            editor.putString("name" + i, playerName.getText().toString());
            editor.putInt("score" + i, score);
            editor.commit();

            popUpBox.dismiss();

            //route user to leader board listing
            Intent intent = new Intent(GameActivity.this, LeaderBoardActivity.class);
            startActivity(intent);
        }

        if(id==R.id.okBtn){
            popUpBoxLose.dismiss();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        game.start();
        ViewGroup gridElement = (ViewGroup) gridView.getChildAt(i);
        ImageView currImg = (ImageView) gridElement.getChildAt(0);
        ImageDTO image = selectedImages.get(i);
        currImg.setImageBitmap(image.getBitmap());
        gameMusic.playClickSound();
        //imageView=(ImageView)findViewById(R.id.gameImageview);
        //Bitmap bitmap = image.getBitmap();
        if(!matchedCards.contains(image.getId())) {
            if (matchCount < 6) {
                if (selectCount == 0) {
                    selectid1 = null;
                    selectid2 = null;
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

                    if (selectid1 != null && selectid2 != null
                            && imageView1 != null && imageView2 != null) {

                        if (selectid1.getBitmap() != selectid2.getBitmap() && imageView1 != null && imageView2 != null) {
                            handler.postDelayed(runnable, 500);
                            gameMusic.playWrongSound();
                        } else if (selectid1.getBitmap() == selectid2.getBitmap()) {
                            matchCount++;
                            matchedCards.add(image.getId());
                            matches.setText(matchCount + "/" + "6");
                            gameMusic.playCorrectSound();
                        }
                    }
                    selectCount = 0;
                }
            }
            if (matchCount == 6) {
                stopGame();
            }
        }
    }

    private void startTimer(){
        final TextView timer = findViewById(R.id.timer);
        countDownTimer = new CountDownTimer(60000, 1000){
            public void onTick(long millisUntilFinished){
                int minutes = (int) millisUntilFinished/60000;
                int seconds = (int) (millisUntilFinished)/1000;
                timer.setText(String.format("%d:%02d", minutes,seconds));
                remainder = millisUntilFinished;
            }
            public void onFinish(){
                stopGame();

            }
        }.start();
    }

    private void totalScore(){
        score = (int) remainder /100;
        if(remainder < 0){
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
        gameMusic.playWinSound();

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
        playerName =(EditText) popUp.findViewById(R.id.nameTxt);
        playerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(playerName.getText().toString().length()==0){
                    submitName.setEnabled(false);
                }else {
                    submitName.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(playerName.getText().toString().length()==0){
                    submitName.setEnabled(false);
                }else {
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

    void stopGame(){
        countDownTimer.cancel();
        if(matchCount == difficulty){
            totalScore();
            popUpDialogue();
        } else{
            popUpDialogueLose();
        }
    }

}
