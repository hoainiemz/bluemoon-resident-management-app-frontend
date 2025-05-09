package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class IconTextField extends HBox {
    public IconTextField(String imgUrl, String Prompt, boolean password, double height) {
        super();
        getStyleClass().add("login-text-field");
        setPrefHeight(height);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5, 10, 5, 10));
        Image img = new Image(imgUrl);
        ImageView icon = new ImageView(img);
        icon.setPreserveRatio(true);
        icon.setFitHeight(getPrefHeight() * 0.6);
        getChildren().addAll(icon);
        if (!password) {
            TextField field = new TextField();
            field.setPromptText(Prompt);
            HBox.setHgrow(field, Priority.ALWAYS);
            field.setMaxHeight(height);
            getChildren().addAll(field);
        } else {
            PasswordField field = new PasswordField();
            field.setPromptText(Prompt);
            field.setMaxHeight(height);
            HBox.setHgrow(field, Priority.ALWAYS);
            getChildren().addAll(field);
        }
    }
}
