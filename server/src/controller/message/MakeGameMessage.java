package controller.message;

class MakeGameMessage {
    private int gameId;
    private char[][] currentState;
    private String opponentName;
    private char currentTurn;
    private char myTurn;

    MakeGameMessage(int gameId, String opponentName, char[][] currentState, char currentTurn, char myTurn) {
        this.gameId = gameId;
        this.opponentName = opponentName;
        this.currentState = currentState;
        this.currentTurn = currentTurn;
        this.myTurn = myTurn;
    }
}
