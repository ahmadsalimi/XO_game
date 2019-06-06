package view;

import controller.Controller;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.*;

public class GameView {
    private static Group root;
    private static Scene scene;

    private Game game;
    private Button undoButton;
    private Button pauseButton;
    private Button stopButton;
    private GridPane gridPane = new GridPane();

    public static Scene getScene() {
        root = new Group();
        scene = new Scene(root, Controller.getCurrentWidth(), Controller.getCurrentHeight());
        return scene;
    }

    public void showGame(Game game) {
        this.game = game;
        HBox container = BoxMaker.makeContainer(scene);
        VBox gridContainer = BoxMaker.makeGameGrid(gridPane);

        Tile[][] tiles = game.getCurrentState();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                gridPane.add(new StackPane(tiles[i][j].getRectangle(), tiles[i][j].getLabel()), j, i);
            }
        }

        VBox menuBox = makeBox();
        container.getChildren().addAll(gridContainer, menuBox);

        root.getChildren().addAll(container);
    }

    private VBox makeBox() {
        VBox box = BoxMaker.makeVertical();
        ImageView imageView = LogoImage.get();
        Label label = new DefaultLabel("opponent username:").getLabel();
        Label oppName = new DefaultLabel(game.getOpponentUserName()).getLabel();

        undoButton = new DefaultButton("Undo", scene).getButton();
        pauseButton = new DefaultButton("Pause", scene).getButton();
        stopButton = new DefaultButton("Stop", scene).getButton();

        Tile currentTurnTile = game.getCurrentTurn();

        StackPane turnPane = new StackPane(currentTurnTile.getRectangle(), currentTurnTile.getLabel());

        box.getChildren().addAll(imageView, label, oppName, undoButton, pauseButton, stopButton, turnPane);

        return box;
    }

    public Button getUndoButton() {
        return undoButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
