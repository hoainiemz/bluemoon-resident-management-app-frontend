package org.example.hellofx.utils;

import javafx.scene.paint.Color;

public class ColorUtils {
    public static Color getFromRGBA(int red, int green, int blue, double opacity) {
        // Validate input values
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255 || opacity < 0 || opacity > 1) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255, Opacity must be between 0 and 1.");
        }

        // Normalize RGB values to the range of 0.0 to 1.0 (required by Color.color)
        double r = (double) red / 255.0;
        double g = (double) green / 255.0;
        double b = (double) blue / 255.0;

        return Color.color(r, g, b, opacity); // Create JavaFX Color object with opacity
    }
}
