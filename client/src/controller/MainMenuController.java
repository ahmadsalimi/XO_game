package controller;

import com.google.gson.JsonSyntaxException;
import controller.message.Message;
import javafx.application.Platform;
import javafx.scene.Scene;
import models.NewGameData;
import view.AlertView;
import view.ErrorType;
import view.MainMenuView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

class MainMenuController {
    private static SocketData socketData;
    private static Thread receiveThread;
    private Scene scene = MainMenuView.getScene();
    private MainMenuView view = new MainMenuView();
    private ResumeMenuController resumeMenuController;
    private ScoreBoardController scoreBoardController;

    void start(SocketData socketData) {
        MainMenuController.socketData = socketData;
        Controller.setScene(scene);

        if (receiveThread == null) {
            receiveThread = new Thread(this::receiveMessage, "main menu receiver");
            receiveThread.start();
        }

        view.showMenuBox();

        view.getNewGameButton().setOnMouseClicked(event -> {
            Optional<NewGameData> result = view.showNewGameDialog();
            result.ifPresent(this::sendNewGameMessage);
        });

        view.getResumeButton().setOnMouseClicked(event ->
                Message.makePausedGamesRequest(socketData.getMessagePort()).sendTo(socketData)
        );

        view.getShowScoreBoardButton().setOnMouseClicked(event ->
                Message.makeScoreBoardRequest(socketData.getMessagePort()).sendTo(socketData)
        );
        view.getQuitButton().setOnMouseClicked(event -> System.exit(0));
    }

    private void receiveMessage() {
        try {
            synchronized (socketData) {
                if (socketData.getMessagePort() == 0) {
                    socketData.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Socket socket = socketData.getSocket();
        BufferedReader socketReader = socketData.getReader();

        while (socket.isConnected()) {
            Message message;
            try {
                synchronized (socketReader) {
                    String json = socketReader.readLine();
                    if (json == null) break;
                    message = Message.fromJson(json);
                }
            } catch (JsonSyntaxException e) {
                AlertView.showError(ErrorType.CLIENT_ERROR, "invalid message from server.");
                continue;
            } catch (IOException e) {
                break;
            }

            switch (message.getMessageType()) {
                case MAKE_GAME:
                    Platform.runLater(() -> new GameController(message).start(socketData));
                    receiveThread = null;
                    return;
                case PAUSED_GAMES_LIST:
                    Platform.runLater(() -> {
                        resumeMenuController = new ResumeMenuController();
                        resumeMenuController.setList(message);
                        resumeMenuController.start(socketData);
                    });
                    break;
                case SCOREBOARD_LIST:
                    Platform.runLater(() -> {
                        scoreBoardController = new ScoreBoardController();
                        scoreBoardController.setList(message);
                        scoreBoardController.start(socketData);
                    });
                    break;
                case ERROR_MESSAGE:
                    AlertView.showError(ErrorType.SERVER_ERROR, message.getErrorMessage().getMessage());
                    break;
            }
        }

        AlertView.showError(ErrorType.SERVER_ERROR, "server disconnected");
    }

    private void sendNewGameMessage(NewGameData data) {
        if (data.getUsername() == null) {
            AlertView.showError(ErrorType.CLIENT_ERROR, "please type a valid");
        }
        Message.makeNewGameMessage(
                socketData.getMessagePort(), data.getUsername(), data.getRow(), data.getColumn()
        ).sendTo(socketData);
    }
}
