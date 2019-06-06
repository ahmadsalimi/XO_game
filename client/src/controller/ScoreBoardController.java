package controller;

import controller.message.Message;
import javafx.scene.Scene;
import view.ScoreBoardView;

class ScoreBoardController {
    private Scene scene = ScoreBoardView.getScene();
    private ScoreBoardView view = new ScoreBoardView();

    void start(SocketData socketData) {
        Controller.setScene(scene);
        view.showLeaderBoard();
        view.getBackButton().setOnMouseClicked(event -> new MainMenuController().start(socketData));
    }

    void setList(Message message) {
        view.setList(message);
    }
}
