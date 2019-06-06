package controller.message;

import javafx.util.Pair;
import models.Position;

public class MapUpdate {
    private Pair<Position, MapUpdateType> updates;
    private char currentTurn;

    public Pair<Position, MapUpdateType> getUpdates() {
        return updates;
    }

    public char getCurrentTurn() {
        return currentTurn;
    }
}
