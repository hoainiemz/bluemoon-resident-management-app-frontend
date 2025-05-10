package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

public class BadgeCell extends ListCell<Badge> {

    @Override
    protected void updateItem(Badge badge, boolean isEmpty) {
        super.updateItem(badge, isEmpty);

        if (isEmpty || badge == null) {
            setGraphic(null);
            setText(null);
        } else {
            // Create an icon
            FontIcon icon = null;
            icon = new FontIcon();
            icon.setStyle(
                    "-fx-icon-color: " + toRgbString(badge.color()) + ";" +
                    "-fx-icon-code: \"" + badge.icon().getDescription() + "\";"
            );

            // Create a styled text with color
            Text textNode = new Text(badge.text());
            if (badge.color() != null) {
                textNode.setFill(badge.color()); // Apply color
            }

            // Apply the color to the ListCell text
            setText(textNode.getText());
            if (badge.color() != null) {
                setTextFill(badge.color()); // Ensure text color applies in ComboBox
            }
            setGraphic(icon);
        }
    }
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }
}
