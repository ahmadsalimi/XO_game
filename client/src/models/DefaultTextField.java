package models;

import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static models.DefaultLabel.DEFAULT_FONT;
import static models.ScreenData.SCALE;

public class DefaultTextField {
    private static final CornerRadii CORNER_RADIUS = new CornerRadii(30 * SCALE);
    private static final Background TEXT_FIELD_BACKGROUND = new Background(
            new BackgroundFill(Color.WHITE, CORNER_RADIUS, Insets.EMPTY)
    );
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CORNER_RADIUS, BorderWidths.DEFAULT)
    );
    private TextField textField;

    public DefaultTextField() {
        textField = new TextField();
        textField.setFont(DEFAULT_FONT);
        textField.setBackground(TEXT_FIELD_BACKGROUND);
        textField.setBorder(DEFAULT_BORDER);
    }

    public TextField getTextField() {
        return textField;
    }
}
