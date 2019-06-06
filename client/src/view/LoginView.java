package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.BoxMaker;
import models.DefaultLabel;
import models.LogoImage;
import models.ScreenData;

public class LoginView {
    private static Group root;
    private static Scene scene;

    private Button loginButton;
    private TextField textField;

    public static Scene getScene() {
        root = new Group();
        scene = new Scene(root, ScreenData.SCENE_WIDTH, ScreenData.SCENE_HEIGHT);
        return scene;
    }

    public void showLoginBox() {
        HBox container = BoxMaker.makeContainer(scene);

        VBox box = makeLoginBox();
        container.getChildren().add(box);

        root.getChildren().addAll(container);
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public String getFieldText() {
        return textField.getText();
    }

    private VBox makeLoginBox() {
        VBox box = BoxMaker.makeVertical();

        ImageView imageView = LogoImage.get();
        Label label = new DefaultLabel("Enter your name:").getLabel();
        HBox fieldBox = BoxMaker.makeFieldBox(scene);

        fieldBox.getChildren().stream().filter(
                node -> node instanceof TextField
        ).forEach(node -> this.textField = (TextField) node);

        fieldBox.getChildren().stream().filter(
                node -> node instanceof Button
        ).forEach(node -> this.loginButton = (Button) node);

        box.getChildren().addAll(imageView, label, fieldBox);

        return box;
    }

    public void clearField() {
        textField.clear();
    }

    public TextField getField() {
        return textField;
    }
}