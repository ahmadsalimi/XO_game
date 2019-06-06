package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Formatter;

public class SocketData {
    private Socket socket;
    private Formatter formatter;
    private BufferedReader reader;
    private int messagePort;

    SocketData(Socket socket) throws IOException {
        this.socket = socket;
        this.formatter = new Formatter(socket.getOutputStream());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Formatter getFormatter() {
        return formatter;
    }

    Socket getSocket() {
        return socket;
    }

    BufferedReader getReader() {
        return reader;
    }

    int getMessagePort() {
        return messagePort;
    }

    synchronized void setMessagePort(int messagePort) {
        this.messagePort = messagePort;
        this.notifyAll();
    }
}
