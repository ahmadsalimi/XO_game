package controller;

import com.google.gson.JsonSyntaxException;
import controller.message.Message;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import models.Game;
import models.Position;
import view.AlertView;
import view.ErrorType;
import view.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

class GameController {
    private static SocketData socketData;
    private Scene scene = GameView.getScene();
    private GameView view = new GameView();
    private Game game;

    GameController(Message message) {
        game = new Game(message.getMakeGameMessage());
    }

    void start(SocketData socketData) {
        GameController.socketData = socketData;
        Controller.setScene(scene);

        new Thread(this::receiveMessage, "game receiver").start();

        view.showGame(game);
        setGameActions();
        setUndoAction();
        setPauseAction();
        setStopAction();
    }

    private void setStopAction() {
        view.getStopButton().setOnMouseClicked(event ->
                Message.makeStopMessage(socketData.getMessagePort(), game.getGameId()).sendTo(socketData)
        );
    }

    private void setPauseAction() {
        view.getPauseButton().setOnMouseClicked(event ->
                Message.makePauseMessage(socketData.getMessagePort(), game.getGameId()).sendTo(socketData)
        );
    }

    private void setUndoAction() {
        view.getUndoButton().setOnMouseClicked(event ->
                Message.makeUndoMessage(socketData.getMessagePort(), game.getGameId()).sendTo(socketData)
        );
    }

    private void setGameActions() {
        for (int i = 0; i < game.getCurrentState().length; i++) {
            for (int j = 0; j < game.getCurrentState()[i].length; j++) {
                final Position position = new Position(i, j);
                StackPane stackPane = (StackPane) view.getGridPane().getChildren()
                        .get(i * game.getCurrentState()[i].length + j);

                stackPane.setOnMouseClicked(event -> {
                    if (game.isMyTurn()) {
                        if (game.getTile(position).getLabel().getText().charAt(0) == 0) {
                            Message.makePutMessage(
                                    socketData.getMessagePort(), game.getGameId(), position
                            ).sendTo(socketData);
                        } else {
                            AlertView.showError(ErrorType.CLIENT_ERROR, "can't put here");
                        }
                    } else {
                        AlertView.showError(ErrorType.CLIENT_ERROR, "it's not your turn");
                    }
                });

                stackPane.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
                stackPane.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
            }
        }
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
                String json = socketReader.readLine();
                if (json == null) break;
                message = Message.fromJson(json);
            } catch (JsonSyntaxException e) {
                AlertView.showError(ErrorType.CLIENT_ERROR, "invalid message from server.");
                continue;
            } catch (IOException e) {
                AlertView.showError(ErrorType.CLIENT_ERROR, "IOException");
                break;
            }

            switch (message.getMessageType()) {
                case MAP_UPDATE_MESSAGE:
                    Platform.runLater(() -> updateMap(message));
                    break;
                case FINISH_GAME:
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = AlertView.show(
                                "Game Over", message.getFinishGameMessage().getState().getMessage()
                        );
                        result.ifPresent(buttonType -> new MainMenuController().start(socketData));
                    });
                    return;
                case PAUSE:
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = AlertView.show("Closing game", "Game paused.");
                        result.ifPresent(buttonType -> new MainMenuController().start(socketData));
                    });
                    return;
                case STOP:
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = AlertView.show("Closing game", "Game stopped.");
                        result.ifPresent(buttonType -> new MainMenuController().start(socketData));
                    });
                    return;
                case ERROR_MESSAGE:
                    AlertView.showError(ErrorType.SERVER_ERROR, message.getErrorMessage().getMessage());
                    break;
            }
        }

        AlertView.showError(ErrorType.SERVER_ERROR, "server disconnected");

    }

    private void updateMap(Message message) {
        game.getTile(
                message.getMapUpdate().getUpdates().getKey()
        ).setValue(message.getMapUpdate().getUpdates().getValue().getChar());
        game.getCurrentTurn().setValue(message.getMapUpdate().getCurrentTurn());
    }
}
