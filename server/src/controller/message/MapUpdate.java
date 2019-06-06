package controller.message;

import models.Pair;
import models.Position;

class MapUpdate {
    private Pair<Position, MapUpdateType> updates;
    private char currentTurn;

    MapUpdate(Pair<Position, MapUpdateType> updates, char currentTurn) {
        this.updates = updates;
        this.currentTurn = currentTurn;
    }
}
