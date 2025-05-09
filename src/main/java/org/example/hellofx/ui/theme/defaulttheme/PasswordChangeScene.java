package org.example.hellofx.ui.theme.defaulttheme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.PasswordChangeController;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.utils.Effects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PasswordChangeScene extends Notificable implements ThemeScene {
    @Autowired
    private PasswordChangeController passwordChangeController;

    private Scene scene;

    protected Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Scene scene) {
        this.scene = scene;
        HBox container = (HBox) scene.lookup("#container");
        StackPane content = (StackPane) scene.lookup("#content");
        content.setAlignment(Pos.TOP_CENTER);
        content.getChildren().clear();
        VBox mainContent = new VBox();
        mainContent.setPrefWidth(content.getPrefWidth());
        mainContent.setMinWidth(content.getPrefWidth());
        mainContent.setMaxWidth(content.getPrefWidth());
        mainContent.setPrefHeight(content.getPrefHeight());
        mainContent.setMinHeight(content.getPrefHeight());
        mainContent.setMaxHeight(content.getPrefHeight());
        content.getChildren().addAll(mainContent);
        mainContent.getStyleClass().clear();
        mainContent.getStyleClass().add("doi-mat-khau");
        TextFlow passwordChange = new TextFlow(new Text("Thay đổi mật khẩu:"));
        passwordChange.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(passwordChange);
        mainContent.setPadding(new Insets(20, 50, 10, 50));
        HBox formContainer = new HBox();
        passwordChange.setPrefHeight(mainContent.getPrefHeight() * 0.1);
        formContainer.setPrefHeight(mainContent.getPrefHeight() * 0.9);
        mainContent.getChildren().addAll(formContainer);
        VBox form = new VBox();
        form.getStyleClass().add("form");
//        form.setPrefHeight(content.getPrefHeight() * 0.5);
        formContainer.getChildren().addAll(form);
        formContainer.setPadding(new Insets(10, 0, 10, 0));
        HBox matKhauCu = new HBox();
        HBox matKhauCuTextContainer = new HBox();
        TextFlow matKhauCuText = new TextFlow(new Text("Mật khẩu cũ:"));
        matKhauCuTextContainer.getChildren().addAll(matKhauCuText);
        matKhauCuTextContainer.setAlignment(Pos.CENTER_LEFT);
        HBox matKhauCuPasswordFieldContainer = new HBox();
        PasswordField matKhauCuPasswordField = new PasswordField();
        matKhauCuPasswordFieldContainer.getChildren().add(matKhauCuPasswordField);
        matKhauCuPasswordFieldContainer.setAlignment(Pos.CENTER_LEFT);
        matKhauCu.getChildren().addAll(matKhauCuTextContainer, matKhauCuPasswordFieldContainer);
        form.setPrefWidth(mainContent.getPrefWidth() * 0.4);
        matKhauCuTextContainer.setPrefWidth(form.getPrefWidth() * 0.5);
        matKhauCuPasswordFieldContainer.setPrefWidth(form.getPrefWidth() * 0.5);

        HBox matKhauMoiTextContainer = new HBox();
        HBox matKhauMoi = new HBox();
        TextFlow matKhauMoiText = new TextFlow(new Text("Mật khẩu mới:"));
        matKhauMoiTextContainer.getChildren().addAll(matKhauMoiText);
        matKhauMoiTextContainer.setAlignment(Pos.CENTER_LEFT);
        HBox matKhauMoiPasswordFieldContainer = new HBox();
        PasswordField matKhauMoiPasswordField = new PasswordField();
        matKhauMoiPasswordFieldContainer.getChildren().add(matKhauMoiPasswordField);
        matKhauMoiPasswordFieldContainer.setAlignment(Pos.CENTER_LEFT);
        matKhauMoi.getChildren().addAll(matKhauMoiTextContainer, matKhauMoiPasswordFieldContainer);
        matKhauMoiTextContainer.setPrefWidth(form.getPrefWidth() * 0.5);
        matKhauMoiPasswordFieldContainer.setPrefWidth(form.getPrefWidth() * 0.5);

        HBox xacNhanMatKhauMoiTextContainer = new HBox();
        HBox xacNhanMatKhauMoi = new HBox();
        TextFlow xacNhanMatKhauMoiText = new TextFlow(new Text("Xác nhận mật khẩu mới:"));
        xacNhanMatKhauMoiTextContainer.getChildren().addAll(xacNhanMatKhauMoiText);
        xacNhanMatKhauMoiTextContainer.setAlignment(Pos.CENTER_LEFT);
        HBox xacNhanMatKhauMoiPasswordFieldContainer = new HBox();
        PasswordField xacNhanMatKhauMoiPasswordField = new PasswordField();
        xacNhanMatKhauMoiPasswordFieldContainer.getChildren().add(xacNhanMatKhauMoiPasswordField);
        xacNhanMatKhauMoiPasswordFieldContainer.setAlignment(Pos.CENTER_LEFT);
        xacNhanMatKhauMoi.getChildren().addAll(xacNhanMatKhauMoiTextContainer, xacNhanMatKhauMoiPasswordFieldContainer);
        xacNhanMatKhauMoiTextContainer.setPrefWidth(form.getPrefWidth() * 0.5);
        xacNhanMatKhauMoiPasswordFieldContainer.setPrefWidth(form.getPrefWidth() * 0.5);
//        xacNhanMatKhauMoi.setStyle("-fx-border-color: transparent transparent rgba(207,207,207,255) transparent;" +
//                                   "-fx-padding: 0 0 10px 0");
        ;
        HBox stateContainer = new HBox();
        TextFlow stateText = new TextFlow(new Text("Password changed successfully!"));
        stateContainer.getChildren().addAll(stateText);
        stateContainer.setAlignment(Pos.CENTER_LEFT);
        stateText.getStyleClass().add("state-text");
        stateText.setVisible(false);

        HBox doiMatKhauButtonContainer = new HBox();
        Button doiMatKhauButton = new Button("Đổi mật khẩu");
        doiMatKhauButton.setPadding(new Insets(10, 20, 10, 20));
        doiMatKhauButton.setOnMouseEntered(event -> {
            doiMatKhauButton.setEffect(Effects.glowEffect);
        });
        doiMatKhauButton.setOnMouseExited(event -> {
            doiMatKhauButton.setEffect(null);
        });
        doiMatKhauButton.setOnAction(event -> {
            String state = passwordChangeController.passwordChangeRequest(matKhauCuPasswordField.getText(), matKhauMoiPasswordField.getText(), xacNhanMatKhauMoiPasswordField.getText());
            Text child = (Text) stateText.getChildren().get(0);
            child.setText(state);
            if (state.equals("Password changed successfully!")) {
                child.setFill(Color.valueOf("#549159"));
                showPopUpMessage("Password changed", "Your password has been changed successfully!");
            } else {
                child.setFill(Color.RED);
                showPopUpMessage("ERROR", "There was an error processing your password reset!");
            }
            stateText.setVisible(true);

//            System.out.println(state);
        });
        doiMatKhauButtonContainer.getChildren().addAll(doiMatKhauButton);
        form.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doiMatKhauButton.fire();
            }
        });

        form.getChildren().addAll(matKhauCu, matKhauMoi, xacNhanMatKhauMoi, stateContainer, doiMatKhauButtonContainer);

        form.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doiMatKhauButton.fire();
            }
        });

        form.setSpacing(10);

        mainContent.setSpacing(20);

        return scene;
    }
}
