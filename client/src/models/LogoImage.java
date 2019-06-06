package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static models.ScreenData.SCALE;

public class LogoImage {
    private static final String IMAGE_PATH = "favicon.png";
    private static Image image;
    private static ImageView imageView;

    static {
        try {
            image = new Image(new FileInputStream(IMAGE_PATH));
            imageView = new ImageView(image);
            imageView.setFitWidth(image.getWidth() * SCALE);
            imageView.setFitHeight(image.getHeight() * SCALE);
            imageView.setCache(true);
            imageView.setMouseTransparent(true);
        } catch (FileNotFoundException ignored) {
        }
    }

    public static Image getImage() {
        return image;
    }

    public static ImageView get() {
        return imageView;
    }
}
