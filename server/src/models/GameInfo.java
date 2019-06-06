package models;

public class GameInfo {
    private String xUsername;
    private String oUsername;
    private int gameId;

    public GameInfo(Game game) {
        this.xUsername = game.getPlayersInfo().get('X').getAccount().getName();
        this.oUsername = game.getPlayersInfo().get('O').getAccount().getName();
        this.gameId = game.getId();
    }
}