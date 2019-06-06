package controller.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.SocketPair;
import models.Account;
import models.GameInfo;
import models.Pair;
import models.Position;

import java.util.ArrayList;

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

    public static Message makeScoreBoardListMessage(int port, Account[] accounts) {
        Message message = new Message(port, MessageType.SCOREBOARD_LIST, port + "_scoreboard");
        message.scoreBoardList = new ScoreBoardList(accounts);
        return message;
    }

    public static Message makePausedGamesListMessage(int port, ArrayList<GameInfo> games) {
        Message message = new Message(port, MessageType.PAUSED_GAMES_LIST, port + "_paused_list");
        message.pausedGamesList = new PausedGamesList(games);
        return message;
    }

    public static Message makeMapUpdateMessage(int port, Pair<Position, MapUpdateType> updates, char currentTurn) {
        Message message = new Message(port, MessageType.MAP_UPDATE_MESSAGE, port + "map_update");
        message.mapUpdate = new MapUpdate(updates, currentTurn);
        return message;
    }

    public static Message makeErrorMessage(int port, String messageId, String errorMessage) {
        Message message = new Message(port, MessageType.ERROR_MESSAGE, "error_" + messageId);
        message.errorMessage = new ErrorMessage(messageId, errorMessage);
        return message;
    }

    public static Message makeDoneMessage(int port, String messageId) {
        return new Message(port, MessageType.DONE_MESSAGE, "done_" + messageId);
    }

    public static Message makeMakeGameMessage(int port, String opponentName, char[][] currentState, int gameId, char currentTurn, char myTurn) {
        Message message = new Message(port, MessageType.MAKE_GAME, port + "_" + opponentName);
        message.makeGameMessage = new MakeGameMessage(gameId, opponentName, currentState, currentTurn, myTurn);
        return message;
    }

    public static Message makeFinishGameMessage(int port, GameState state) {
        Message message = new Message(port, MessageType.FINISH_GAME, port + "_" + state);
        message.finishGameMessage = new FinishGameMessage(state);
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

    public static Message makePortMessage(int port) {
        return new Message(port, MessageType.PORT_MESSAGE, port + "_update");
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

    public String getMessageId() {
        return messageId;
    }

    public LoginMessage getLoginMessage() {
        return loginMessage;
    }

    public NewGameMessage getNewGameMessage() {
        return newGameMessage;
    }

    public PutMessage getPutMessage() {
        return putMessage;
    }

    public OtherGameActions getOtherGameActions() {
        return otherGameActions;
    }

    public void sendTo(SocketPair socketPair) {
        synchronized (socketPair) {
            socketPair.getFormatter().format(this.toJson() + "\n");
            socketPair.getFormatter().flush();
        }
    }
}
