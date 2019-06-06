package models;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class LeaderBoardCell {
    private HBox box;

    public LeaderBoardCell(Account account, int rank) {
        Label infoLabel = new DefaultLabel(rank + ". " + account.getName() +
                ", wins: " + account.getNumberOfWinnings() +
                ", draws: " + account.getNumberOfDraws() +
                ", losses: " + account.getNumberOfLosses()
        ).getLabel();
        box = BoxMaker.makeSimpleHorizontal();
        box.getChildren().addAll(infoLabel);
    }

    public HBox getBox() {
        return box;
    }
}
