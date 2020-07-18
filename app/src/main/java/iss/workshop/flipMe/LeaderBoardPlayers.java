package iss.workshop.flipMe;

public class LeaderBoardPlayers implements Comparable<LeaderBoardPlayers>{
    private int playerRank;
    private String playerName;
    private int playerScore;


    public LeaderBoardPlayers() {
    }

    public LeaderBoardPlayers(String playerName, int playerScore){
        this.playerName = playerName;
        this.playerScore = playerScore;
    }

    public int getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public int compareTo(LeaderBoardPlayers other) {
        if(this.playerScore < other.playerScore){
            return 1;
        }  else {
            return -1;
        }
    }
}

