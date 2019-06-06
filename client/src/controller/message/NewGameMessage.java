package controller.message;

class NewGameMessage {
    private String opponentUsername;
    private int row;
    private int column;

    NewGameMessage(String opponentUsername, int row, int column) {
        this.opponentUsername = opponentUsername;
        this.row = row;
        this.column = column;
    }
}
