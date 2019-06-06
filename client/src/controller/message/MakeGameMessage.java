package controller.message;

public class MakeGameMessage {
    private int gameId;
    private char[][] currentState;
    private String opponentName;
    private char currentTurn;
    private char myTurn;

    public int getGameId() {
        return gameId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public char[][] getCurrentState() {
        return currentState;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }

    public char getMyTurn() {
        return myTurn;
    }
}
