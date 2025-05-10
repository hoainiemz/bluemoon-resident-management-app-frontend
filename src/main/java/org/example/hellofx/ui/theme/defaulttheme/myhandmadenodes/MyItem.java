package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MyItem extends VBox {
    private Button button;

    public Button getButton() {
        return button;
    }

    public MyItem(String name) {
        super();
        Label itemName = new Label(name);

        button = new Button("Xem");

        itemName.getStyleClass().add("my-item-name");
        button.getStyleClass().add("my-item-button");
        getStyleClass().add("my-item");

        getChildren().addAll(itemName, button);
    }
}
