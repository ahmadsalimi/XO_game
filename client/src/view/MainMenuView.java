package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.*;

import java.util.Optional;

import static models.ScreenData.SCALE;

public class MainMenuView {
    private static final Insets DIALOG_PADDING = new Insets(20 * SCALE);
    private static final Insets DIALOG_GRID_PADDING = new Insets(20 * SCALE, 150 * SCALE, 10 * SCALE, 10 * SCALE);
    private static final double DIALOG_SPACING = 20 * SCALE;
    private static final double GRID_GAP = 10 * SCALE;
    private static final int MIN_DIMENSION = 3;
    private static final int MAX_DIMENSION = 30;
    private static Group root;
    private static Scene scene;

    private Button newGameButton;
    private Button resumeButton;
    private Button showScoreBoardButton;
    private Button quitButton;

    public static Scene getScene() {
        root = new Group();
        scene = new Scene(root, Controller.getCurrentWidth(), Controller.getCurrentHeight());
        return scene;
    }

    public void showMenuBox() {
        HBox container = BoxMaker.makeContainer(scene);

        VBox box = makeBox();
        container.getChildren().add(box);

        root.getChildren().addAll(container);
    }

    private VBox makeBox() {
        VBox box = BoxMaker.makeVertical();
        ImageView imageView = LogoImage.get();

        newGameButton = new DefaultButton("New game", scene).getButton();
        resumeButton = new DefaultButton("Resume", scene).getButton();
        showScoreBoardButton = new DefaultButton("Leaderboard", scene).getButton();
        quitButton = new DefaultButton("Quit", scene).getButton();

        box.getChildren().addAll(imageView, newGameButton, resumeButton, showScoreBoardButton, quitButton);
        return box;
    }

    public Button getNewGameButton() {
        return newGameButton;
    }

    public Button getResumeButton() {
        return resumeButton;
    }

    public Button getShowScoreBoardButton() {
        return showScoreBoardButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }

    public Optional<NewGameData> showNewGameDialog() {
        Dialog<NewGameData> dialog = new Dialog<>();
        dialog.setTitle("New game");

        VBox box = makeDialogBox();

        Label header = new DefaultLabel("Enter opponent's username and dimensions of game:").getLabel();

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);
        GridPane gridPane = makeDialogGridPane();

        Label usernameLabel = new DefaultLabel("username:").getLabel();
        TextField usernameTextField = new DefaultTextField().getTextField();

        Platform.runLater(usernameTextField::requestFocus);

        Label rowLabel = new DefaultLabel("row:").getLabel();
        Spinner<Integer> rowSpinner = makeSpinner();

        Label columnLabel = new DefaultLabel("column:").getLabel();
        Spinner<Integer> columnSpinner = makeSpinner();

        gridPane.addColumn(0, usernameLabel, rowLabel, columnLabel);
        gridPane.addColumn(1, usernameTextField, rowSpinner, columnSpinner);

        box.getChildren().addAll(header, gridPane);
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(button -> {
            if (button == okButtonType) {
                return new NewGameData(usernameTextField.getText(), rowSpinner.getValue(), columnSpinner.getValue());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    private VBox makeDialogBox() {
        VBox box = new VBox();
        box.setPadding(DIALOG_PADDING);
        box.setSpacing(DIALOG_SPACING);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private GridPane makeDialogGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(GRID_GAP);
        gridPane.setVgap(GRID_GAP);
        gridPane.setPadding(DIALOG_GRID_PADDING);
        return gridPane;
    }

    private Spinner<Integer> makeSpinner() {
        Spinner<Integer> spinner = new Spinner<>(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_DIMENSION, MAX_DIMENSION, MIN_DIMENSION)
        );

        spinner.getEditor().setFont(DefaultLabel.DEFAULT_FONT);
        return spinner;
    }
}
