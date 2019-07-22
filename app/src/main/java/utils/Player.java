package utils;

import java.io.Serializable;

public class Player implements Serializable {

    private String playerName;
    private int playerAvatar;
    private int playerScore;

    public Player(String playerName, int playerAvatar) {
        this.playerName = playerName;
        this.playerAvatar = playerAvatar;
        this.playerScore = 0;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerAvatar(int playerAvatar) {
        this.playerAvatar = playerAvatar;
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

    public int getPlayerAvatar() {
        return playerAvatar;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", playerAvatar=" + playerAvatar +
                ", playerScore=" + playerScore +
                '}';
    }
}
