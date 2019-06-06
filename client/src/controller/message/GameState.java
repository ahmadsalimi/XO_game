package controller.message;

public enum GameState {
    WIN("You won!"), LOSE("You Lost!"), DRAW("Draw!!");

    private final String message;

    GameState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
