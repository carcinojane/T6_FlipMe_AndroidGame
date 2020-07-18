package iss.workshop.flipMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity
        implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

//        SharedPreferences pref = getSharedPreferences("players",MODE_PRIVATE);
//        pref.getString("name","");
//        pref.getInt("score",0);

        Button backBtn = findViewById(R.id.backBtn);
        if(backBtn!=null){
            backBtn.setOnClickListener(this);
        }

        leaderBoardListing();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.backBtn){
            Intent intent = new Intent(LeaderBoardActivity.this, MainActivity.class);
            startActivity(intent);
        }
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




}