package models;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static models.ScreenData.SCALE;

public class BoxMaker {
    private static final CornerRadii CORNER_RADIUS = new CornerRadii(30 * SCALE);
    private static final Color SHADOW_COLOR = Color.rgb(102, 128, 128, 0.5);
    private static final Insets DEFAULT_PADDING = new Insets(40 * SCALE);
    private static final double DEFAULT_SPACING = 20 * SCALE;
    private static final double SCROLL_MIN_WIDTH = 800 * SCALE;
    private static final double BOX_MAX_WIDTH = 700 * SCALE;
    private static final double BOX_MAX_HEIGHT = 900 * SCALE;
    private static final double GRID_GAP = 10 * SCALE;
    private static final double CONTAINER_SPACING = 30 * SCALE;
    private static final double GAME_GRID_SIZE = 1300 * SCALE;
    private static final double FIELD_BOX_SPACING = 50 * SCALE;
    private static final DropShadow SHADOW = new DropShadow(
            BlurType.TWO_PASS_BOX, SHADOW_COLOR, 30 * SCALE, 50 * SCALE, 0, 0
    );
    private static final Background CONTAINER_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(242, 255, 232), CornerRadii.EMPTY, Insets.EMPTY)
    );
    private static final Background BOX_BACKGROUND = new Background(
            new BackgroundFill(Color.rgb(244, 244, 244), CORNER_RADIUS, Insets.EMPTY)
    );
    private static final Border DEFAULT_BORDER = new Border(
            new BorderStroke(Color.gray(0.4), BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );

    public static VBox makeVertical() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(DEFAULT_PADDING);
        box.setMaxSize(BOX_MAX_WIDTH, BOX_MAX_HEIGHT);
        box.setBackground(BOX_BACKGROUND);
        box.setSpacing(DEFAULT_SPACING);

        box.setEffect(SHADOW);
        return box;
    }

    private static HBox makeHorizontal() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(DEFAULT_PADDING);
        box.setMaxSize(BOX_MAX_WIDTH, BOX_MAX_HEIGHT);
        box.setBackground(BOX_BACKGROUND);
        box.setSpacing(DEFAULT_SPACING);

        box.setEffect(SHADOW);
        return box;
    }

    public static HBox makeContainer(Scene scene) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.resize(scene.getWidth(), scene.getHeight());
        container.prefWidthProperty().bind(scene.widthProperty());
        container.prefHeightProperty().bind(scene.heightProperty());
        container.setBackground(CONTAINER_BACKGROUND);
        container.setSpacing(CONTAINER_SPACING);

        return container;
    }

    public static VBox makeGameGrid(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(GRID_GAP);
        gridPane.setHgap(GRID_GAP);

        VBox gridContainer = new VBox(gridPane);
        gridContainer.setMinSize(GAME_GRID_SIZE, GAME_GRID_SIZE);
        gridContainer.setAlignment(Pos.CENTER);

        return gridContainer;
    }

    public static HBox makeFieldBox(Scene scene) {
        HBox fieldBox = new HBox();
        fieldBox.setSpacing(FIELD_BOX_SPACING);
        fieldBox.setAlignment(Pos.CENTER);

        TextField textField = new DefaultTextField().getTextField();
        Button button = new DefaultButton("Login", scene).getButton();

        fieldBox.getChildren().addAll(textField, button);

        return fieldBox;
    }

    public static HBox makeSidedBox(ScrollPane scrollPane, Button backButton) {
        HBox box = BoxMaker.makeHorizontal();

        ImageView imageView = LogoImage.get();

        VBox leftSidebar = BoxMaker.makeLeftSideBar();
        leftSidebar.getChildren().addAll(backButton, imageView);

        scrollPane.setMinWidth(SCROLL_MIN_WIDTH);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        box.getChildren().addAll(leftSidebar, scrollPane);

        return box;
    }

    private static VBox makeLeftSideBar() {
        VBox leftSidebar = new VBox();
        leftSidebar.setAlignment(Pos.CENTER_LEFT);
        leftSidebar.setPadding(DEFAULT_PADDING);
        leftSidebar.setSpacing(DEFAULT_SPACING);
        return leftSidebar;
    }

    static HBox makeSimpleHorizontal() {
        HBox box = new HBox();
        box.setSpacing(DEFAULT_SPACING);
        box.setPadding(DEFAULT_PADDING);
        box.setAlignment(Pos.CENTER);
        box.setBorder(DEFAULT_BORDER);
        return box;
    }

    public static VBox makeSimpleVertical() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
