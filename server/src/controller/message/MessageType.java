package controller.message;

public enum MessageType {
    // client -> server
    LOGIN,
    NEW_GAME,
    RESUME_GAME,
    SCOREBOARD_REQUEST,
    PAUSED_GAMES_REQUEST,
    PUT,
    UNDO,
    PAUSE,
    STOP,

    // server -> client
    SCOREBOARD_LIST,
    PAUSED_GAMES_LIST,
    MAP_UPDATE_MESSAGE,
    ERROR_MESSAGE,
    MAKE_GAME,
    DONE_MESSAGE,
    FINISH_GAME,
    PORT_MESSAGE
}
