package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.DefaultLabel;

import java.util.Optional;

import static models.ScreenData.SCALE;

public class AlertView {
    private static final double ALERT_WIDTH = 400 * SCALE;
    private static final double ALERT_HEIGHT = 150 * SCALE;

    public static void showError(ErrorType errorType, String message) {
        System.out.println(message);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setTitle("Error");
            alert.setHeaderText(errorType.getMessage());
            alert.setResizable(false);
            alert.showAndWait();
        });
    }

    public static Optional<ButtonType> show(String title, String message) {
        Dialog<ButtonType> resultDialog = new Dialog<>();
        resultDialog.setTitle(title);
        resultDialog.setResizable(false);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        resultDialog.getDialogPane().getButtonTypes().addAll(okButtonType);

        Label label = new DefaultLabel(message).getLabel();
        VBox container = new VBox(label);
        container.setMinSize(ALERT_WIDTH, ALERT_HEIGHT);
        container.setAlignment(Pos.CENTER);

        resultDialog.getDialogPane().setContent(container);

        resultDialog.setResultConverter(button -> okButtonType);

        return resultDialog.showAndWait();
    }
}
