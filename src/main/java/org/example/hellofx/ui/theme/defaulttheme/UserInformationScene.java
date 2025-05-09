package org.example.hellofx.ui.theme.defaulttheme;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.ProfileController;
import org.example.hellofx.controller.UserInformationController;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.model.enums.GenderType;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndComboBox;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndDateTimePicker;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class UserInformationScene extends Notificable{
    @Autowired
    private UserInformationController userInformationController;
    @Autowired
    private final ProfileController profileController;

    private Scene scene;
    private Account profile;
    private Resident resident;

    public UserInformationScene(ProfileController profileController) {
        this.profileController = profileController;
    }

    protected Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Account reprofile, Resident reresident, Scene scene) {
        profile = reprofile;
        resident = reresident;
        this.scene = scene;
        Pane container = (Pane) scene.lookup("#container");
        StackPane content = (StackPane) scene.lookup("#content");
        content.getChildren().clear();
        VBox mainContent = new VBox();

        ScrollPane scrollPane = new ScrollPane(mainContent);

        content.getChildren().addAll(scrollPane);
        mainContent.setPrefWidth(content.getPrefWidth());
        mainContent.setMinWidth(content.getPrefWidth());
        mainContent.setMaxWidth(content.getPrefWidth());
        mainContent.setMinHeight(content.getPrefHeight());

        scrollPane.setPrefWidth(content.getPrefWidth());
        scrollPane.setMinWidth(content.getPrefWidth());
        scrollPane.setMaxWidth(content.getPrefWidth());
        scrollPane.setPrefHeight(content.getPrefHeight());
        scrollPane.setMinHeight(content.getPrefHeight());
        scrollPane.setMaxHeight(content.getPrefHeight());

        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setId("main-content");
        TextFlow section = new TextFlow(new Text("Thông tin cá nhân:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));
        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        HBox publicProfile = new HBox();
        mainContent.getChildren().addAll(publicProfile);
        publicProfile.setPrefHeight(content.getPrefHeight() * 0.2);
        publicProfile.setMinWidth(mainContent.getPrefWidth() * 0.8);
        publicProfile.setPrefWidth(mainContent.getPrefWidth() * 0.8);
        publicProfile.setMaxWidth(mainContent.getPrefWidth() * 0.8);
        publicProfile.setAlignment(Pos.CENTER_LEFT);
        publicProfile.setPadding(new Insets(20, 50, 20, 50));

        Image userIcon = new Image("images/user-icon.jpg");
        ImageView userImg = new ImageView(userIcon);
        userImg.setPreserveRatio(true);
        publicProfile.getChildren().addAll(userImg);
        userImg.setFitHeight(publicProfile.getPrefHeight() * 0.9);
        publicProfile.getStyleClass().add("public-profile");
        publicProfile.setSpacing(30);
        VBox publicInfo = new VBox();
        publicProfile.getChildren().addAll(publicInfo);
        publicInfo.getChildren().add(new TextFlow(new Label("ID cư dân: " + resident.getResidentId())));
        ((Label)((TextFlow) publicInfo.getChildren().get(0)).getChildren().get(0)).setStyle("-fx-font-weight: bold; -fx-font-size: 30px");
        publicInfo.getChildren().add(new TextFlow(new Label("BlueMoon member since " + resident.getMoveInDate())));
//        publicInfo.getChildren().add(new TextFlow(new Label("Email: " + profile.getEmail())));
        publicInfo.getChildren().add(new TextAndTextField("Email cá nhân: ", profile.getEmail(), "Enter your email", "top-email", false)); //profileController.getProfile().getRole() != AccountType.Resident || profile.getId().equals(profileController.getProfile().getId())));
//        publicInfo.getChildren().add(new TextFlow(new Label("Phone: " + profile.getPhone())));
        publicInfo.getChildren().add(new TextAndTextField("Số điện thoại: ", profile.getPhone(), "Enter your phone", "top-phone", false, true)); //profileController.getProfile().getRole() != AccountType.Resident || profile.getId().equals(profileController.getProfile().getId())));
        publicInfo.setAlignment(Pos.TOP_LEFT);
        publicInfo.setSpacing(15);
        mainContent.setSpacing(20);
        if (profileController.getProfile().getRole() == AccountType.Resident && !profile.getUserId().equals(profileController.getProfile().getUserId())) {
            HBox actionContainer = new HBox();
            Button close = new Button("Đóng");
            close.setId("close");
            close.getStyleClass().add("cancel-button");
            actionContainer.getChildren().addAll(close);
            actionContainer.setAlignment(Pos.CENTER_RIGHT);
            mainContent.getChildren().addAll(actionContainer);
            actionContainer.setPrefWidth(mainContent.getPrefWidth() * 0.8);
            actionContainer.setMaxWidth(mainContent.getPrefWidth() * 0.8);
            actionContainer.setMinWidth(mainContent.getPrefWidth() * 0.8);
            return scene;
        }

        VBox privateProfile = new VBox();
        mainContent.getChildren().addAll(privateProfile);
//        privateProfile.setPrefHeight(mainContent.getPrefHeight() * 0.2);
//        VBox.setVgrow(privateProfile, Priority.ALWAYS);
        privateProfile.setMinWidth(mainContent.getPrefWidth() * 0.8);
        privateProfile.setPrefWidth(mainContent.getPrefWidth() * 0.8);
        privateProfile.setMaxWidth(mainContent.getPrefWidth() * 0.8);
        privateProfile.setAlignment(Pos.CENTER_LEFT);
        privateProfile.setPadding(new Insets(20, 50, 20, 50));
        privateProfile.getStyleClass().add("public-profile");
        mainContent.setSpacing(20);
        privateProfile.setSpacing(20);

        HBox doubleTab = new HBox();
        privateProfile.getChildren().addAll(doubleTab);
        doubleTab.setPrefWidth(privateProfile.getPrefWidth());
        VBox leftTab = new VBox(), rightTab = new VBox();
        doubleTab.getChildren().addAll(leftTab, rightTab);
        doubleTab.setSpacing(doubleTab.getPrefWidth() * 0.1);
        leftTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        rightTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        leftTab.setAlignment(Pos.TOP_CENTER);
        rightTab.setAlignment(Pos.TOP_CENTER);
        ComboBox<GenderType> genderComboBox = new ComboBox<>(FXCollections.observableArrayList(GenderType.values()));
        if (resident.getGender() != null) {
            genderComboBox.setValue(resident.getGender());
        }
        genderComboBox.setEditable(false);
        ComboBox<AccountType> roleComboBox = new ComboBox<>(FXCollections.observableArrayList(AccountType.Client, AccountType.Resident));
        roleComboBox.setValue(profile.getRole());
        roleComboBox.setEditable(false);
//        leftTab.getChildren().add(new VerticleTextAndTextField("ID tài khoản: ", profile.getId().toString(), "Enter your ID", "id-info", false));
        if (profile.getRole() != AccountType.Admin) {
            leftTab.getChildren().add(new VerticleTextAndComboBox("Vai trò: ", roleComboBox, "Vai trò", "role-info", profileController.getProfile().getRole() == AccountType.Admin));
        }
        else {
            leftTab.getChildren().add(new VerticleTextAndComboBox("Vai trò: ", roleComboBox, "Vai trò", "role-info", false));
        }
        leftTab.getChildren().add(new VerticleTextAndTextField("Tên: ", resident.getFirstName(), "Enter your first name", "first-name-info", true));
        leftTab.getChildren().add(new VerticleTextAndComboBox("Giới tính: ", genderComboBox, "Enter your gender", "gender-info", true));
        rightTab.getChildren().add(new VerticleTextAndDateTimePicker("Ngày sinh(mm/dd/yyyy): ", resident.getDateOfBirth(), "Enter your birthday", "birthday-info", true));
        rightTab.getChildren().add(new VerticleTextAndTextField("Họ: ", resident.getLastName(), "Enter your last name", "last-name-info", true));

        privateProfile.getChildren().add(new VerticleTextAndTextField("Số căn cước công dân: ", resident.getIdentityCard(), "Enter your identity card", "identity-card-info", true));
        privateProfile.getChildren().add(new VerticleTextAndTextField("Email cá nhân: ", profile.getEmail(), "Enter your email", "email-info", true));
        privateProfile.getChildren().add(new VerticleTextAndTextField("Số điện thoại: ", profile.getPhone(), "Enter your phone number", "phone-info", true, false));
//        mainContent.setStyle("-fx-background-color: red");

        HBox buttonContainer = new HBox();
        Button close = new Button("Đóng");
        close.setId("close");
        close.getStyleClass().add("cancel-button");
        mainContent.getChildren().addAll(buttonContainer);
        Button save = new Button("Lưu");
        buttonContainer.getChildren().addAll(save);
        if (JavaFxApplication.getCurrentScene() != scene) {
            buttonContainer.getChildren().addAll(close);
        }
        buttonContainer.setPrefWidth(mainContent.getPrefWidth() * 0.8);
        buttonContainer.setMaxWidth(mainContent.getPrefWidth() * 0.8);
        buttonContainer.setMinWidth(mainContent.getPrefWidth() * 0.8);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setSpacing(20);
//        buttonContainer.setStyle("-fx-background-color: red;");
        save.setId("save-button");
        save.setPrefWidth(150);
        save.setOnAction(event -> {
            String email = ((VerticleTextAndTextField) scene.lookup("#email-info")).getTextField().getText();
            String phone = ((VerticleTextAndTextField) scene.lookup("#phone-info")).getTextField().getText();
            AccountType role = (AccountType) ((VerticleTextAndComboBox) scene.lookup("#role-info")).getComboBox().getValue();
            Account currAccount = new Account(profile.getUserId(), profile.getUsername(), email, phone, profile.getPasswordHash(), role);

            LocalDate date = ((VerticleTextAndDateTimePicker) scene.lookup("#birthday-info ")).getDatePicker().getValue();
            String firstName = ((VerticleTextAndTextField) scene.lookup("#first-name-info")).getTextField().getText();
            String lastName = ((VerticleTextAndTextField) scene.lookup("#last-name-info")).getTextField().getText();
            GenderType gender = (GenderType) ((VerticleTextAndComboBox) scene.lookup("#gender-info")).getComboBox().getValue();
            String identityCard = ((VerticleTextAndTextField) scene.lookup("#identity-card-info")).getTextField().getText();
            Resident curResident = new Resident(resident.getUserId(), firstName, lastName, gender, date, identityCard, resident.getMoveInDate());

            Validation vl = userInformationController.emailCheck(email);

            if (!email.equals(profile.getEmail()) && vl.state().equals(ValidationState.ERROR)) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }

            vl = userInformationController.phoneCheck(phone);

            if (!phone.equals(profile.getPhone()) && vl.state().equals(ValidationState.ERROR)) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }

            vl = userInformationController.identityCardCheck(identityCard);
            if (identityCard == null || identityCard.isEmpty() || (!identityCard.equals(resident.getIdentityCard()) && vl.state().equals(ValidationState.ERROR))) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            profileController.residentProfileUpdateRequest(curResident);
            profileController.accountProfileUpdateRequest(currAccount);
            if (JavaFxApplication.getCurrentScene() != scene) {
                close.fire();
                return;
            }

            ((TextAndTextField) scene.lookup("#top-email")).getTextField().setText(email);
            ((TextAndTextField) scene.lookup("#top-phone")).getTextField().setText(phone);
            profile = profileController.findAccountByUserId(profile.getUserId());
            resident = profileController.findResidentByUserId(profile.getUserId());
            Text welcome = ((Text) scene.lookup("#welcome"));
            if (profileController.getResident().getFirstName() != null) {
                welcome.setText("Chào mừng bạn, " + profileController.getResident().getFirstName());
            }
            else {
                welcome.setText("Chào mừng bạn, " + profileController.getProfileNameRequest());
            }
            showPopUpMessage("SUCCESS", "Đã thay đổi thông tin cá nhân!");
        });
        return scene;
    }
}
