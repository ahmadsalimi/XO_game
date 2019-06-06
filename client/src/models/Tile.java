package models;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;

import static models.ScreenData.SCALE;

public class Tile {
    private static final double DEFAULT_SIZE = 300 * SCALE;
    private static final String DEFAULT_FONT_FAMILY = "SansSerif";
    private static final double DEFAULT_FONT_SIZE = 300 * SCALE;
    private static final HashMap<Character, Color> textFills = new HashMap<>();
    private static final HashMap<Character, Color> backgrounds = new HashMap<>();

    static {
        textFills.put('X', Color.rgb(0, 112, 255));
        textFills.put('O', Color.rgb(255, 50, 0));
        textFills.put((char) 0, Color.WHITE);

        backgrounds.put('X', Color.rgb(255, 204, 0));
        backgrounds.put('O', Color.rgb(152, 255, 0));
        backgrounds.put((char) 0, Color.rgb(255, 203, 158));
    }

    private Rectangle rectangle;
    private Label label;

    Tile(char value, double scale) {
        this.rectangle = new Rectangle(DEFAULT_SIZE * scale, DEFAULT_SIZE * scale);
        rectangle.setArcWidth(rectangle.getWidth());
        rectangle.setArcHeight(rectangle.getHeight());
        this.label = new Label();
        label.setFont(Font.font(DEFAULT_FONT_FAMILY, FontWeight.EXTRA_BOLD, DEFAULT_FONT_SIZE * scale));
        setValue(value);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Label getLabel() {
        return label;
    }

    public void setValue(char value) {
        label.setText(String.valueOf(value));
        label.setTextFill(textFills.get(value));
        rectangle.setFill(backgrounds.get(value));
    }
}

