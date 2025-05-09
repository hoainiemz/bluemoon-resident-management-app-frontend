package org.example.hellofx.ui.theme.defaulttheme;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.SignUpController;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.utils.ScreenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignUpScene extends Notificable implements ThemeScene {
    @Autowired
    private SignUpController signUpController;

    private Scene scene;

    protected Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Scene scene) {
        this.scene = scene;
        VBox leftFrame = (VBox) scene.lookup("#leftFrame");
        leftFrame.setAlignment(Pos.TOP_CENTER);
        leftFrame.getChildren().clear();
        leftFrame.setPadding(new Insets(60, 0, 0, 0));
        VBox welcomeFrame = new VBox();
        Text welcomeText = new Text("Chào mừng bạn!");
        welcomeText.getStyleClass().add("welcomeText");
        Text welcomeText2 = new Text("Tạo tài khoản mới");
        welcomeText2.getStyleClass().add("welcomeText2");
        welcomeFrame.getChildren().addAll(welcomeText, welcomeText2);

        welcomeFrame.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
        welcomeFrame.setSpacing(5);
        leftFrame.setSpacing(30);

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
                }
                else {
                    PasswordField field = new PasswordField();
                    field.setPromptText(Prompt);
                    field.setMinWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setPrefWidth(leftFrame.getPrefWidth() * 0.8);
                    field.setMaxWidth(leftFrame.getPrefWidth() * 0.8);
                    textFieldContainer.getChildren().addAll(field);
                }
                Label status = new Label("OK!");
                status.getStyleClass().add("status-text");
                status.setVisible(false);
                status.setPadding(new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
                vBox.getChildren().addAll(textFieldContainer, status);
                vBox.setId(id);
                return vBox;
            }
        };
        loginForm.setSpacing(15);
        loginForm.getChildren().add(iconTextFieldMaker.func("images/user-icon-grey.png", "Nhập tài khoản", false, "reg-username-field"));
        loginForm.getChildren().add(iconTextFieldMaker.func("images/password-icon.png", "Nhập mật khẩu", true, "reg-password-field"));
        loginForm.getChildren().add(iconTextFieldMaker.func("images/email.png", "nhập địa chỉ email của bạn", false, "reg-email-field"));
        loginForm.getChildren().add(iconTextFieldMaker.func("images/phone.png", "nhập số điện thoại của bạn", false, "reg-phone-field"));

        HBox buttonContainer = new HBox();
        buttonContainer.setId("buttonContainer");
        buttonContainer.setPrefHeight(ScreenUtils.getScreenHeight() * 0.1);

        Button signUpButton = new Button("Đăng ký");
        signUpButton.getStyleClass().add("login-button");
        Button backToLogin = new Button("Quay lại đăng nhập");
        backToLogin.getStyleClass().add("register-button");
        buttonContainer.getChildren().addAll(signUpButton, backToLogin);
        VBox.setMargin(buttonContainer, new Insets(0, leftFrame.getPrefWidth() * 0.2, 0, leftFrame.getPrefWidth() * 0.2));
        signUpButton.setPrefWidth(leftFrame.getPrefWidth() * 0.4);
        backToLogin.setPrefWidth(leftFrame.getPrefWidth() * 0.4);
        backToLogin.setPrefHeight(buttonContainer.getPrefHeight() * 0.5);
        buttonContainer.setSpacing(20);


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

        backToLogin.setOnAction(actionEvent -> {
            signUpController.backToSignInClicked();
        });

        signUpButton.setOnAction(actionEvent -> {
            String username = ((TextField) ((HBox) ((VBox) loginForm.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).getText();
            String password = ((TextField) ((HBox) ((VBox) loginForm.getChildren().get(1)).getChildren().get(0)).getChildren().get(1)).getText();
            String email = ((TextField) ((HBox) ((VBox) loginForm.getChildren().get(2)).getChildren().get(0)).getChildren().get(1)).getText();
            String phone = ((TextField) ((HBox) ((VBox) loginForm.getChildren().get(3)).getChildren().get(0)).getChildren().get(1)).getText();
            List<String> res = signUpController.signUpClicked(username, password, email, phone);
            Boolean kt = true;
            for (int i = 0; i < res.size(); i++) {
                VBox container = (VBox) loginForm.getChildren().get(i);
                Label status = (Label) container.getChildren().get(1);
                if (res.get(i).equals("OK!")) {
                    status.setTextFill(Color.valueOf("#549159"));
                }
                else {
                    status.setTextFill(Color.RED);
                    kt = false;
                }
                status.setText(res.get(i));
                status.setVisible(true);
            }
            if (kt) {
                showPopUpMessage("Registration Successfully", "Chúc mừng, tài khoản của bạn đã được đăng ký thành công!");
            }
            else {
                showPopUpMessage("ERROR", "Có lỗi xảy ra!");
            }
        });

        Button quenMatKhau = new Button("Quên mật khẩu");
        quenMatKhau.setOnAction(event -> {
            signUpController.forgotPasswordClicked();
        });
        TextFlow forgotPasswordContainer = new TextFlow(new Text("Bạn quên mật khẩu của mình? "), quenMatKhau);
        forgotPasswordContainer.setId("forgotPasswordContainer");

        leftFrame.getChildren().addAll(welcomeFrame, loginForm, buttonContainer, forgotPasswordContainer, freeze, shutDownContainer);
        leftFrame.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                signUpButton.fire();
            }
        });
        return scene;
    }
}
