package controller.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.SocketData;
import models.Position;

public class Message {
    private int port;
    private MessageType messageType;
    private String messageId;
    // client -> server
    private LoginMessage loginMessage;
    private NewGameMessage newGameMessage;
    // in game requests
    private PutMessage putMessage;
    private OtherGameActions otherGameActions; // undo, pause, resume, stop
    // server -> client
    private ScoreBoardList scoreBoardList;
    private PausedGamesList pausedGamesList;
    private MapUpdate mapUpdate;
    private ErrorMessage errorMessage;
    private MakeGameMessage makeGameMessage;
    private FinishGameMessage finishGameMessage;

    private Message(int port, MessageType messageType, String messageId) {
        this.port = port;
        this.messageType = messageType;
        this.messageId = messageId;
    }

    public static Message makeLoginMessage(int port, String username) {
        Message message = new Message(port, MessageType.LOGIN, port + "_" + username);
        message.loginMessage = new LoginMessage(username);
        return message;
    }

    public static Message makeNewGameMessage(int port, String opponentUserName, int row, int column) {
        Message message = new Message(port, MessageType.NEW_GAME, port + "_" + opponentUserName);
        message.newGameMessage = new NewGameMessage(opponentUserName, row, column);
        return message;
    }

    public static Message makeResumeMessage(int port, int gameId) {
        Message message = new Message(port, MessageType.RESUME_GAME, port + "_" + gameId);
        message.otherGameActions = new OtherGameActions(gameId);
        return message;
    }

    public static Message makeScoreBoardRequest(int port) {
        return new Message(port, MessageType.SCOREBOARD_REQUEST, port + "_scoreboard_request");
    }

    public static Message makePausedGamesRequest(int port) {
        return new Message(port, MessageType.PAUSED_GAMES_REQUEST, port + "_paused_request");
    }

    public static Message makePutMessage(int port, int gameId, Position position) {
        Message message = new Message(port, MessageType.PUT, port + "_" + gameId + "_" + position.toString());
        message.putMessage = new PutMessage(gameId, position);
        return message;
    }

    public static Message makeUndoMessage(int port, int gameId) {
        Message message = new Message(port, MessageType.UNDO, port + "_" + gameId + "_undo");
        message.otherGameActions = new OtherGameActions(gameId);
        return message;
    }

    public static Message makePauseMessage(int port, int gameId) {
        Message message = new Message(port, MessageType.PAUSE, port + "_" + gameId + "_pause");
        message.otherGameActions = new OtherGameActions(gameId);
        return message;
    }

    public static Message makeStopMessage(int port, int gameId) {
        Message message = new Message(port, MessageType.STOP, port + "_" + gameId + "_stop");
        message.otherGameActions = new OtherGameActions(gameId);
        return message;
    }

    public static Message fromJson(String json) throws JsonSyntaxException {
        return new Gson().fromJson(json, Message.class);
    }

    private String toJson() {
        return new Gson().toJson(this);
    }

    public int getPort() {
        return port;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public ScoreBoardList getScoreBoardList() {
        return scoreBoardList;
    }

    public PausedGamesList getPausedGamesList() {
        return pausedGamesList;
    }

    public MapUpdate getMapUpdate() {
        return mapUpdate;
    }

    public MakeGameMessage getMakeGameMessage() {
        return makeGameMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public FinishGameMessage getFinishGameMessage() {
        return finishGameMessage;
    }

    public void sendTo(SocketData socketData) {
        synchronized (socketData) {
            socketData.getFormatter().format(this.toJson() + "\n");
            socketData.getFormatter().flush();
        }
    }
}
