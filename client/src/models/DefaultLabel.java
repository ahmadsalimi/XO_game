package models;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static models.ScreenData.SCALE;

public class DefaultLabel extends Label {
    public static final Font DEFAULT_FONT = Font.font("SansSerif", FontWeight.BOLD, 30 * SCALE);
    private Label label;

    public DefaultLabel(String info) {
        label = new Label(info);
        label.setFont(DEFAULT_FONT);
    }

    public Label getLabel() {
        return label;
    }
}
