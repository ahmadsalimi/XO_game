package models;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static models.DefaultLabel.DEFAULT_FONT;
import static models.ScreenData.SCALE;

public class DefaultButton extends Button {
    private static final double DEFAULT_BUTTON_OPACITY = 0.2;
    private static final double HOVERED_BUTTON_OPACITY = 1.0;
    private static final CornerRadii CORNER_RADIUS = new CornerRadii(30 * SCALE);
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.SOLID, CORNER_RADIUS, BorderWidths.DEFAULT)
    );
    private static final Background BUTTON_DEFAULT_BACKGROUND = new Background(
            new BackgroundFill(
                    Color.rgb(255, 235, 158, DEFAULT_BUTTON_OPACITY), CORNER_RADIUS, Insets.EMPTY
            )
    );
    private Button button;

    public DefaultButton(String text, Scene scene) {
        button = new Button(text);
        button.setFont(DEFAULT_FONT);
        button.setBackground(BUTTON_DEFAULT_BACKGROUND);
        button.setBorder(DEFAULT_BORDER);
        AnimationTimer onHoverAnimation = makeOnHoverAnimation(button);
        AnimationTimer onExitAnimation = makeOnExitAnimation(button);
        button.setOnMouseEntered(event -> {
            scene.setCursor(Cursor.HAND);
            onHoverAnimation.start();
        });
        button.setOnMouseExited(event -> {
            scene.setCursor(Cursor.DEFAULT);
            onExitAnimation.start();
        });
    }

    public Button getButton() {
        return button;
    }

    private AnimationTimer makeOnExitAnimation(Button button) {
        return new AnimationTimer() {
            private double currentOpacity = HOVERED_BUTTON_OPACITY;

            @Override
            public void handle(long now) {
                if (currentOpacity >= DEFAULT_BUTTON_OPACITY) {
                    button.setBackground(
                            new Background(
                                    new BackgroundFill(
                                            Color.rgb(255, 235, 158, currentOpacity),
                                            CORNER_RADIUS,
                                            Insets.EMPTY
                                    )
                            )
                    );

                    currentOpacity -= 0.08;
                } else {
                    this.stop();
                    currentOpacity = HOVERED_BUTTON_OPACITY;
                }
            }
        };
    }

    private AnimationTimer makeOnHoverAnimation(Button button) {
        return new AnimationTimer() {
            private double currentOpacity = DEFAULT_BUTTON_OPACITY;

            @Override
            public void handle(long now) {
                if (currentOpacity <= HOVERED_BUTTON_OPACITY) {
                    button.setBackground(
                            new Background(
                                    new BackgroundFill(
                                            Color.rgb(255, 235, 158, currentOpacity),
                                            CORNER_RADIUS,
                                            Insets.EMPTY
                                    )
                            )
                    );

                    currentOpacity += 0.08;
                } else {
                    this.stop();
                    currentOpacity = DEFAULT_BUTTON_OPACITY;
                }
            }
        };
    }
}
