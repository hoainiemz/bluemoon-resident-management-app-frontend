package org.example.hellofx.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class ScreenUtils {

    public static double getScreenWidth() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return screenBounds.getWidth();
    }

    public static double getScreenHeight() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return screenBounds.getHeight();
    }
}