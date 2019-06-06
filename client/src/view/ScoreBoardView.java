package view;

import controller.Controller;
import controller.message.Message;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Account;
import models.BoxMaker;
import models.DefaultButton;
import models.LeaderBoardCell;

import java.util.ArrayList;

public class ScoreBoardView {
    private static Group root;
    private static Scene scene;

    private ScrollPane scrollPane;
    private Button backButton = new DefaultButton("Back", scene).getButton();

    public static Scene getScene() {
        root = new Group();
        scene = new Scene(root, Controller.getCurrentWidth(), Controller.getCurrentHeight());
        return scene;
    }

    public void showLeaderBoard() {
        HBox container = BoxMaker.makeContainer(scene);

        HBox box = BoxMaker.makeSidedBox(scrollPane, backButton);
        container.getChildren().add(box);

        root.getChildren().addAll(container);

    }

    public void setList(Message message) {
        Account[] gamesInfo = message.getScoreBoardList().getAccounts();
        ArrayList<HBox> boxesList = new ArrayList<>();
        for (int i = 0; i < gamesInfo.length; i++) {
            LeaderBoardCell cell = new LeaderBoardCell(gamesInfo[i], i + 1);
            boxesList.add(cell.getBox());
        }

        HBox[] boxes = new HBox[boxesList.size()];
        boxes = boxesList.toArray(boxes);

        VBox content = BoxMaker.makeSimpleVertical();
        content.getChildren().addAll(boxes);

        scrollPane = new ScrollPane();
        scrollPane.setContent(content);
    }

    public Button getBackButton() {
        return backButton;
    }
}
