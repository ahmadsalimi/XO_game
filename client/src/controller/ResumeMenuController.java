package controller;

import controller.message.Message;
import javafx.scene.Scene;
import models.PausedGameBox;
import view.ResumeMenuView;

import java.util.ArrayList;

class ResumeMenuController {
    private static SocketData socketData;
    private Scene scene = ResumeMenuView.getScene();
    private ResumeMenuView view = new ResumeMenuView();

    void start(SocketData socketData) {
        ResumeMenuController.socketData = socketData;
        Controller.setScene(scene);
        view.showResumeList();
        view.getBackButton().setOnMouseClicked(event -> new MainMenuController().start(socketData));
    }

    private void setPlayActions() {
        ArrayList<PausedGameBox> pausedGames = view.getPausedGames();
        pausedGames.forEach(
                pausedGameBox -> pausedGameBox.getPlayButton().setOnMouseClicked(
                        event -> Message.makeResumeMessage(
                                socketData.getMessagePort(), pausedGameBox.getGameId()
                        ).sendTo(socketData)
                )
        );
    }

    void setList(Message message) {
        view.setList(message);
        setPlayActions();
    }
}
