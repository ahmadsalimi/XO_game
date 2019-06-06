package controller.message;

public class NewGameMessage {
    private String opponentUsername;
    private int row;
    private int column;

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
