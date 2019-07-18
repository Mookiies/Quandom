package utils;

import android.widget.ImageView;

public class Player {

    String playerName;
    ImageView playerAvatar;
    int playerScore;

    public Player(String playerName, ImageView playerAvatar) {
        this.playerName = playerName;
        this.playerAvatar = playerAvatar;
        this.playerScore = 0;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerAvatar(ImageView playerAvatar) {
        this.playerAvatar = playerAvatar;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ImageView getPlayerAvatar() {
        return playerAvatar;
    }

    public int getPlayerScore() {
        return playerScore;
    }
}
