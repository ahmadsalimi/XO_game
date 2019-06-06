package controller.message;

import models.Position;

class PutMessage {
    private int gameId;
    private Position position;

    PutMessage(int gameId, Position position) {
        this.gameId = gameId;
        this.position = position;
    }
}
