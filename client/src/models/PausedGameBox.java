package models;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class PausedGameBox {
    private Button playButton;
    private HBox box;
    private int gameId;

    public PausedGameBox(GameInfo info, Scene scene) {
        Label infoLabel = new DefaultLabel("X player: " + info.getXUsername() + ", " +
                "O player: " + info.getOUsername()
        ).getLabel();
        playButton = new DefaultButton("Play", scene).getButton();
        box = BoxMaker.makeSimpleHorizontal();
        box.getChildren().addAll(infoLabel, playButton);
        gameId = info.getGameId();
    }

    public Button getPlayButton() {
        return playButton;
    }

    public HBox getBox() {
        return box;
    }

    public int getGameId() {
        return gameId;
    }
}
