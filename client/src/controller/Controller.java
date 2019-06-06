package controller;

import com.google.gson.JsonSyntaxException;
import controller.message.Message;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.LogoImage;
import models.ScreenData;
import view.AlertView;
import view.ErrorType;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    private static final String IP = "localhost";
    private static final int PORT = 5555;
    private static SocketData socketData;
    private static Stage stage;

    static void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public static double getCurrentWidth() {
        return stage.getScene().getWidth();
    }

    public static double getCurrentHeight() {
        return stage.getScene().getHeight();
    }

    public void main(Stage stage) {
        Controller.stage = stage;

        try {
            socketData = new SocketData(new Socket(IP, PORT));
        } catch (IOException e) {
            AlertView.showError(ErrorType.CLIENT_ERROR, "cannot connect to server.");
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::closeSocket, "shut down hook"));

        Platform.runLater(this::receiveMessage);

        LoginController loginController = new LoginController();
        loginController.start(socketData);

        setStageProperties();
        stage.show();
    }

    private void setStageProperties() {
        stage.setTitle("XO game");
        stage.getIcons().add(LogoImage.getImage());
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.setMinWidth(ScreenData.SCENE_WIDTH);
        stage.setMinHeight(ScreenData.SCENE_HEIGHT);
    }

    private void closeSocket() {
        try {
            socketData.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
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
                case PORT_MESSAGE:
                    socketData.setMessagePort(message.getPort());
                    return;
                case ERROR_MESSAGE:
                    AlertView.showError(ErrorType.SERVER_ERROR, message.getErrorMessage().getMessage());
                    break;
            }
        }

        AlertView.showError(ErrorType.SERVER_ERROR, "server disconnected");
    }
}
