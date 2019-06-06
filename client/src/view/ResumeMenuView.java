package view;

import controller.Controller;
import controller.message.Message;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.BoxMaker;
import models.DefaultButton;
import models.GameInfo;
import models.PausedGameBox;

import java.util.ArrayList;

public class ResumeMenuView {
    private static Group root;
    private static Scene scene;

    private ScrollPane scrollPane = new ScrollPane();
    private ArrayList<PausedGameBox> pausedGames = new ArrayList<>();
    private Button backButton = new DefaultButton("Back", scene).getButton();

    public static Scene getScene() {
        root = new Group();
        scene = new Scene(root, Controller.getCurrentWidth(), Controller.getCurrentHeight());
        return scene;
    }

    public void showResumeList() {
        HBox container = BoxMaker.makeContainer(scene);

        HBox box = BoxMaker.makeSidedBox(scrollPane, backButton);
        container.getChildren().add(box);

        root.getChildren().addAll(container);
    }


    public void setList(Message message) {
        ArrayList<GameInfo> gamesInfo = message.getPausedGamesList().getGames();
        ArrayList<HBox> boxesList = new ArrayList<>();
        for (GameInfo info : gamesInfo) {
            PausedGameBox box = new PausedGameBox(info, scene);
            pausedGames.add(box);
            boxesList.add(box.getBox());
        }

        HBox[] boxes = new HBox[boxesList.size()];
        boxes = boxesList.toArray(boxes);

        VBox content = BoxMaker.makeSimpleVertical();
        content.getChildren().addAll(boxes);

        scrollPane.setContent(content);
    }

    public ArrayList<PausedGameBox> getPausedGames() {
        return pausedGames;
    }

    public Button getBackButton() {
        return backButton;
    }
}
