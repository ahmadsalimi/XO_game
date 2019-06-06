package models;

public class NewGameData {
    private String username;
    private int row;
    private int column;

    public NewGameData(String username, int row, int column) {
        this.username = username;
        this.row = row;
        this.column = column;
    }

    public String getUsername() {
        return username;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
