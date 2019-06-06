package models;

public class PlayerInfo {
    private Account account;
    private boolean undoUsed = false;
    private Position lastPosition;

    PlayerInfo(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    boolean isUndoUsed() {
        return undoUsed;
    }

    void setUndoUsed() {
        this.undoUsed = true;
    }

    Position getLastPosition() {
        return lastPosition;
    }

    void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }
}
