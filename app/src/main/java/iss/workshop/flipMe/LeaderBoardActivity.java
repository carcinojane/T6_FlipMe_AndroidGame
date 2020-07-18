package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener, ServiceConnection{
    MusicService musicService;
    String currentSong;

    private static final String TAG = "Swipe Position";
    private float x1, x2, y1, y2;
    private static int MIN_DISTANCE = 150;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        //initialize gesture detector
        this.gestureDetector = new GestureDetector(LeaderBoardActivity.this, this);

//        SharedPreferences pref = getSharedPreferences("players",MODE_PRIVATE);
//        pref.getString("name","");
//        pref.getInt("score",0);
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        currentSong=getIntent().getStringExtra("currentSong");


        leaderBoardListing();
    }

    void leaderBoardListing(){
        List<LeaderBoardPlayers> leaderBoardPlayersList = getLeaderBoardPlayersList();
        Collections.sort(leaderBoardPlayersList);
        int rank = 1;
//        for(LeaderBoardPlayers player : leaderBoardPlayersList){
//            player.setPlayerRank(rank++);
//        }

        for (int i = 0; i < leaderBoardPlayersList.size();i++){
            leaderBoardPlayersList.get(i).setPlayerRank(rank++);
        }

        ListView listView = findViewById(R.id.leaderBoardListing);
        LeaderBoardAdapter adapter = new LeaderBoardAdapter(this,R.layout.activity_leader_board,leaderBoardPlayersList);
        listView.setAdapter(adapter);
    }

    ArrayList<LeaderBoardPlayers> getLeaderBoardPlayersList(){
        ArrayList<LeaderBoardPlayers> playerList = new ArrayList<>();
        System.out.println(playerList.size());
        SharedPreferences pref = getSharedPreferences("players", MODE_PRIVATE);
        int lastIndex, i = 0;
        while (true){
            if(!pref.contains("name"+i)){
                break;
            }
            i++;
        }
        lastIndex = i;
        for(i = 0; i < lastIndex; i++){
            LeaderBoardPlayers player = new LeaderBoardPlayers(pref.getString("name" + i,""),pref.getInt("score" + i,0));
            playerList.add(player);
        }
        return playerList;

//        if(pref.contains("name") && pref.contains("score")){
//            LeaderBoardPlayers players = new LeaderBoardPlayers(pref.getString("name",""),pref.getInt("score",0));
//            playerList.add(players);
//        }
//        System.out.println(playerList.size());
//        return  playerList;

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

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.LocalBinder binder=(MusicService.LocalBinder) iBinder;
        if(binder!=null){
            musicService=binder.getService();
            musicService.playMenuSong();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}