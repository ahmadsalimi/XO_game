package models;

import javafx.stage.Screen;

public class ScreenData {
    public static final double SCENE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth() * 0.75;
    public static final double SCENE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight() * 0.75;
    private static final double DEFAULT_HEIGHT = 2160 * 0.75;
    public static final double SCALE = SCENE_HEIGHT / DEFAULT_HEIGHT;
}