package controller;

import com.google.gson.JsonSyntaxException;
import controller.message.Message;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import view.AlertView;
import view.ErrorType;
import view.LoginView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

class LoginController {
    private static Scene scene = LoginView.getScene();
    private static LoginView view = new LoginView();
    private static SocketData socketData;

    void start(SocketData socketData) {
        LoginController.socketData = socketData;
        Controller.setScene(scene);

        new Thread(this::receiveMessage, "login receiver").start();

        view.showLoginBox();

        view.getLoginButton().setOnMouseClicked(event -> sendLoginMessage());
        view.getField().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) sendLoginMessage();
        });
    }

    private void sendLoginMessage() {
        if (view.getFieldText().length() < 3) {
            AlertView.showError(ErrorType.CLIENT_ERROR, "Please enter at least 4 characters");
            return;
        }
        Message.makeLoginMessage(socketData.getMessagePort(), view.getFieldText()).sendTo(socketData);
        view.clearField();
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
                break;
            }

            switch (message.getMessageType()) {
                case DONE_MESSAGE:
                    Platform.runLater(() -> new MainMenuController().start(socketData));
                    return;
                case ERROR_MESSAGE:
                    AlertView.showError(ErrorType.SERVER_ERROR, message.getErrorMessage().getMessage());
                    break;
            }
        }

        AlertView.showError(ErrorType.SERVER_ERROR, "server disconnected");
    }
}

