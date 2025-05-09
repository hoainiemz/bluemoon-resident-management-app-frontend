package org.example.hellofx.ui.theme.defaulttheme;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.ForgotPasswordController;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.utils.ScreenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordScene extends Notificable implements ThemeScene {
    @Autowired
    ForgotPasswordController forgotPasswordController;

    private Scene scene;

    String realCode = null;

    protected Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Scene scene) {
        realCode = null;
        this.scene = scene;
        VBox leftFrame = (VBox) scene.lookup("#leftFrame");
        leftFrame.setAlignment(Pos.CENTER);
        leftFrame.getChildren().clear();
        leftFrame.setPadding(new Insets(60, 0, 0, 0));

        VBox welcomeFrame = new VBox();
        Text welcomeText = new Text("Lấy lại mật khẩu");
        welcomeText.getStyleClass().add("welcomeText");
        Text welcomeText2 = new Text("Nhập email của bạn để đặt lại mật khẩu.");
        welcomeText2.getStyleClass().add("welcomeText2");
        welcomeFrame.getChildren().addAll(welcomeText, new TextFlow(welcomeText2));
        welcomeFrame.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
        welcomeFrame.setSpacing(5);
        leftFrame.setSpacing(25);

        HBox shutDownContainer = new HBox();
        shutDownContainer.setId("shutDownContainer");
        HBox freeze = new HBox();
        freeze.setId("freeze");
        Button shutDownButton = new Button("Đóng ứng dụng");
        shutDownButton.getStyleClass().addAll();
        shutDownContainer.getChildren().addAll(shutDownButton);
        shutDownContainer.setAlignment(Pos.CENTER_LEFT);
//        shutDownContainer.setStyle("-fx-background-color: yellow;");
        shutDownContainer.setPrefWidth(leftFrame.getPrefWidth() * 0.9);
        shutDownContainer.setMaxWidth(leftFrame.getPrefWidth() * 0.9);
        shutDownContainer.setMinWidth(leftFrame.getPrefWidth() * 0.9);
        shutDownButton.getStyleClass().add("shutdown-button"); // Link to CSS
        shutDownButton.setOnAction(e -> Platform.exit()); // Closes app

        VBox loginForm = new VBox();
        loginForm.setId("loginForm");


        MyFun iconTextFieldMaker = new MyFun() {

            public VBox func(String imgUrl, String Prompt, boolean password, String id) {
                VBox vBox = new VBox();
                HBox textFieldContainer = new HBox();
                textFieldContainer.getStyleClass().add("login-text-field");
                textFieldContainer.setPrefHeight(loginForm.getPrefHeight() * 0.15);
                textFieldContainer.setAlignment(Pos.CENTER);
                VBox.setMargin(textFieldContainer, new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                textFieldContainer.setPadding(new Insets(5, 10, 5, 10));
                Image img = new Image(imgUrl);
                ImageView icon = new ImageView(img);
                icon.setPreserveRatio(true);
                icon.setFitHeight(textFieldContainer.getPrefHeight() * 0.6);
                textFieldContainer.getChildren().addAll(icon);
                if (!password) {
                    TextField field = new TextField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                else {
                    PasswordField field = new PasswordField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                Label status = new Label("OK!");
                status.getStyleClass().add("status-text");
                status.setVisible(false);
                status.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                vBox.getChildren().addAll(textFieldContainer, status);
                return vBox;
            }
        };
        loginForm.setSpacing(15);
        loginForm.getChildren().add(iconTextFieldMaker.func("images/user-icon-grey.png", "Nhập địa chỉ email", false, "email-field"));

        HBox buttonContainer = new HBox();
        buttonContainer.setId("buttonContainer");
        buttonContainer.setPrefHeight(ScreenUtils.getScreenHeight() * 0.1);
        Button sendResetEmailButton = new Button("Gửi mã xác nhận");
        sendResetEmailButton.setId("sendResetEmailButton");
        buttonContainer.getChildren().addAll(sendResetEmailButton);
        sendResetEmailButton.setOnAction(e -> {
            String email = ((TextField) leftFrame.lookup("#email-field")).getText();
            Validation response = forgotPasswordController.emailCheck(email);
            if (!response.state().equals(ValidationState.OK)) {
                showPopUpMessage("ERROR", response.message());
                return;
            }
            try {
                realCode = forgotPasswordController.sendResetMail(email);
                showPopUpMessage("Kiểm tra email của bạn!", "Vui lòng kiểm tra địa chỉ email " + email + " để lấy mã xác nhận");
                nextStep();
            } catch (Exception ex) {
                showPopUpMessage("ERROR", "Có lỗi xảy ra trong quá trình gửi mail");
            }
        });
//        buttonContainer.setStyle("-fx-background-color: red;");
        buttonContainer.setAlignment(Pos.CENTER);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setPrefWidth(leftFrame.getPrefWidth() * 0.9);
        separator.setMinWidth(leftFrame.getPrefWidth() * 0.9);
        separator.setMaxWidth(leftFrame.getPrefWidth() * 0.9);
        sendResetEmailButton.setPrefWidth(leftFrame.getPrefWidth() * 0.9);
        sendResetEmailButton.setPrefHeight(ScreenUtils.getScreenHeight() * 0.04);
        Button quayLaiDangNhap = new Button("Đăng nhập");
        quayLaiDangNhap.setOnAction(event -> {
            forgotPasswordController.loginClicked();
        });
        TextFlow forgotPasswordContainer = new TextFlow(new Label("Bạn đã có tài khoản? "), quayLaiDangNhap);
        forgotPasswordContainer.setId("forgotPasswordContainer");

        leftFrame.getChildren().addAll(welcomeFrame, loginForm, separator, buttonContainer, forgotPasswordContainer, freeze, shutDownContainer);

        return scene;
    }

    private void nextStep() {
        VBox loginForm = (VBox) scene.lookup("#loginForm");
        ((TextField) loginForm.lookup("#email-field")).setDisable(true);
        VBox leftFrame = (VBox) scene.lookup("#leftFrame");
        MyFun iconTextFieldMaker = new MyFun() {

            public VBox func(String imgUrl, String Prompt, boolean password, String id) {
                VBox vBox = new VBox();
                HBox textFieldContainer = new HBox();
                textFieldContainer.getStyleClass().add("login-text-field");
                textFieldContainer.setPrefHeight(loginForm.getPrefHeight() * 0.15);
                textFieldContainer.setAlignment(Pos.CENTER);
                VBox.setMargin(textFieldContainer, new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                textFieldContainer.setPadding(new Insets(5, 10, 5, 10));
                Image img = new Image(imgUrl);
                ImageView icon = new ImageView(img);
                icon.setPreserveRatio(true);
                icon.setFitHeight(textFieldContainer.getPrefHeight() * 0.6);
                textFieldContainer.getChildren().addAll(icon);
                if (!password) {
                    TextField field = new TextField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                else {
                    PasswordField field = new PasswordField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                Label status = new Label("OK!");
                status.getStyleClass().add("status-text");
                status.setVisible(false);
                status.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                vBox.getChildren().addAll(textFieldContainer, status);
                return vBox;
            }
        };
        loginForm.getChildren().add(iconTextFieldMaker.func("images/verify-icon.png", "Nhập mã xác nhận", false, "code-field"));
        Button sendResetEmailButton = (Button) leftFrame.lookup("#sendResetEmailButton");
        sendResetEmailButton.setText("Lấy lại mật khẩu");
        sendResetEmailButton.setOnAction(event -> {
            String code = ((TextField) leftFrame.lookup("#code-field")).getText();
           if (code.equals(realCode)) {
               final_step();
           }
           else {
               showPopUpMessage("ERROR", "Mã xác nhận không chính xác");
           }
        });
    }

    private void final_step() {
        VBox loginForm = (VBox) scene.lookup("#loginForm");
        ((TextField) loginForm.lookup("#email-field")).setDisable(true);
        VBox leftFrame = (VBox) scene.lookup("#leftFrame");
        MyFun iconTextFieldMaker = new MyFun() {
            public VBox func(String imgUrl, String Prompt, boolean password, String id) {
                VBox vBox = new VBox();
                HBox textFieldContainer = new HBox();
                textFieldContainer.getStyleClass().add("login-text-field");
                textFieldContainer.setPrefHeight(loginForm.getPrefHeight() * 0.15);
                textFieldContainer.setAlignment(Pos.CENTER);
                VBox.setMargin(textFieldContainer, new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                textFieldContainer.setPadding(new Insets(5, 10, 5, 10));
                Image img = new Image(imgUrl);
                ImageView icon = new ImageView(img);
                icon.setPreserveRatio(true);
                icon.setFitHeight(textFieldContainer.getPrefHeight() * 0.6);
                textFieldContainer.getChildren().addAll(icon);
                if (!password) {
                    TextField field = new TextField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                else {
                    PasswordField field = new PasswordField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                    field.setId(id);
                }
                Label status = new Label("OK!");
                status.getStyleClass().add("status-text");
                status.setVisible(false);
                status.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                vBox.getChildren().addAll(textFieldContainer, status);
                return vBox;
            }
        };
        loginForm.getChildren().remove(loginForm.getChildren().size() - 1);
        loginForm.getChildren().add(iconTextFieldMaker.func("images/password-icon.png", "Nhập mật khẩu mới", true, "password-field"));

        Button sendResetEmailButton = (Button) leftFrame.lookup("#sendResetEmailButton");
        sendResetEmailButton.setText("Thay đổi mật khẩu");
        sendResetEmailButton.setOnAction(event -> {
            String email = ((TextField) leftFrame.lookup("#email-field")).getText();
            String pass = ((PasswordField) leftFrame.lookup("#password-field")).getText();
            forgotPasswordController.resetRequest(email, pass);
            try {
                showPopUpMessage("OK!", "Đổi mật khẩu thành công!");
                ((PasswordField) leftFrame.lookup("#password-field")).setDisable(true);
                sendResetEmailButton.setDisable(true);
            }
            catch (Exception e) {
                showPopUpMessage("ERROR", "Có lỗi xảy ra");
            }
        });
    }
}
