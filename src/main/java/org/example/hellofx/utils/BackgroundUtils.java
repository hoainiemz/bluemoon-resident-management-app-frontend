package org.example.hellofx.utils;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class BackgroundUtils {
    public static Background getBackgroundFromImage(String url) {
        Image image = new Image(url);

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.REPEAT,      // Repeat X axis
                BackgroundRepeat.REPEAT,      // Repeat Y axis
                BackgroundPosition.DEFAULT,  // Position
                BackgroundSize.DEFAULT       // Size - Important: Use COVER
        );

        return new Background(backgroundImage);
    }

    public static Background getBackgroundFromColor(Color color) {
        BackgroundFill backgroundFill = new BackgroundFill(color, null, null);

        // Create a Background object using the BackgroundFill.
        Background background = new Background(backgroundFill);

        return background;
    }
}
