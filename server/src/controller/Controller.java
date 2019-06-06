package controller;

import com.google.gson.JsonSyntaxException;
import controller.message.Message;
import models.exception.ClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Controller {
    private static final Scanner consoleScanner = new Scanner(System.in);
    private static final int PORT = 5555;
    private DataHandler dataHandler = DataHandler.getInstance();
    private ServerSocket serverSocket;

    public void main() {
        try {
            serverSocket = new ServerSocket(PORT);
            consoleThread();
            handleClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClients() throws IOException {
        while (true) {
            SocketPair socketPair = new SocketPair(serverSocket.accept());

            new Thread(() -> {
                try {
                    dataHandler.addSocket(socketPair);
                    handleClient(socketPair);
                    dataHandler.removeSocket(socketPair.getSocket());
                } catch (IOException | ClientException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void handleClient(SocketPair socketPair) throws IOException {
        Socket socket = socketPair.getSocket();
        BufferedReader socketScanner = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (socket.isConnected()) {
            Message message;
            try {
                String json = socketScanner.readLine();
                if (json == null) {
                    break;
                }
                message = Message.fromJson(json);
            } catch (JsonSyntaxException e) {
                continue;
            } catch (SocketException e) {
                break;
            }

            try {
                switch (message.getMessageType()) {
                    case LOGIN:
                        dataHandler.login(message);
                        break;
                    case NEW_GAME:
                        dataHandler.newGame(message);
                        break;
                    case RESUME_GAME:
                        dataHandler.resumeGame(message);
                        break;
                    case SCOREBOARD_REQUEST:
                        dataHandler.sendScoreBoardList(message);
                        break;
                    case PAUSED_GAMES_REQUEST:
                        dataHandler.sendPausedGames(message);
                        break;
                    case PUT:
                        dataHandler.put(message);
                        break;
                    case UNDO:
                        dataHandler.undo(message);
                        break;
                    case PAUSE:
                        dataHandler.pause(message);
                        break;
                    case STOP:
                        dataHandler.stop(message);
                        break;
                }
            } catch (ClientException e) {
                Message.makeErrorMessage(message.getPort(), message.getMessageId(), e.getMessage()).sendTo(socketPair);
            }
        }
    }

    private void consoleThread() {
        new Thread(() -> {
            while (true) {
                Thread.yield();
                if (consoleScanner.nextLine().equals("poweroff")) {
                    System.exit(0);
                }
            }
        }).start();
    }
}
