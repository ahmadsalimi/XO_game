package models;

import controller.message.MakeGameMessage;

public class Game {
    private static final double DEFAULT_DIMENSION = 3;
    private int gameId;
    private Tile[][] currentState;
    private String opponentUserName;
    private Tile currentTurn;
    private char myTurn;

    public Game(MakeGameMessage game) {
        this.gameId = game.getGameId();
        int row = game.getCurrentState().length;
        int column = game.getCurrentState()[0].length;
        currentState = new Tile[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                currentState[i][j] = new Tile(
                        game.getCurrentState()[i][j], DEFAULT_DIMENSION / Math.max(row, column)
                );
            }
        }
        this.opponentUserName = game.getOpponentName();
        this.currentTurn = new Tile(game.getCurrentTurn(), 0.6);
        this.myTurn = game.getMyTurn();
    }

    public int getGameId() {
        return gameId;
    }

    public Tile[][] getCurrentState() {
        return currentState;
    }

    public String getOpponentUserName() {
        return opponentUserName;
    }

    public Tile getCurrentTurn() {
        return currentTurn;
    }

    public boolean isMyTurn() {
        return myTurn == currentTurn.getLabel().getText().charAt(0);
    }

    public Tile getTile(Position position) {
        return currentState[position.getRow()][position.getColumn()];
    }
}
