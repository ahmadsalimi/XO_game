package controller.message;

class FinishGameMessage {
    private GameState state;

    FinishGameMessage(GameState state) {
        this.state = state;
    }
}
