package utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.malcolmscruggs.quandom.R;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {

    private String playerName;
    private int playerColor;
    private int playerScore;

    public Player(String playerName, int playerColor) {
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.playerScore = 0;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void increasePlayerScoreOnce() {
        this.playerScore++;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", playerColor=" + playerColor +
                ", playerScore=" + playerScore +
                '}';
    }

    @Override
    public int compareTo(Player o) {
        return playerScore - o.getPlayerScore();
    }
}
