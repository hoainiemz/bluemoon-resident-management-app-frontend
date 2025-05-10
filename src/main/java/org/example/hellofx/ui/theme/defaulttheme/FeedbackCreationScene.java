package org.example.hellofx.ui.theme.defaulttheme;


import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.FeedbackCreationController;
import org.example.hellofx.model.Feedback;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.NotificationType;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.FeedbackService;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.*;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FeedbackCreationScene extends Notificable implements ThemeScene {
    @Autowired
    private FeedbackCreationController controller;

    Scene scene;
    private VBox mainContent;
    private ScrollPane scrollPane;
    @Autowired
    private FeedbackService feedbackService;

    protected Scene getCurrentScene() {
        return scene;
    }

    void reset() {
        scrollPane = null;
        mainContent = null;
    }

    public Scene getScene(Scene scene) {
        reset();
        this.scene = scene;
        Pane container = (Pane) scene.lookup("#container");
        StackPane content = (StackPane) scene.lookup("#content");
        content.getChildren().clear();
        mainContent = new VBox();

        scrollPane = new ScrollPane();
        scrollPane.setContent(mainContent);

        content.getChildren().addAll(scrollPane);
        mainContent.setPrefWidth(content.getPrefWidth());
        mainContent.setMinWidth(content.getPrefWidth());
        mainContent.setMaxWidth(content.getPrefWidth());
//        mainContent.setPrefHeight(content.getPrefHeight());
        mainContent.setMinHeight(content.getPrefHeight());
//        mainContent.setMaxHeight(content.getPrefHeight());

        scrollPane.setPrefWidth(content.getPrefWidth());
        scrollPane.setMinWidth(content.getPrefWidth());
        scrollPane.setMaxWidth(content.getPrefWidth());
        scrollPane.setPrefHeight(content.getPrefHeight());
        scrollPane.setMinHeight(content.getPrefHeight());
        scrollPane.setMaxHeight(content.getPrefHeight());

        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setId("main-content");
        TextFlow section = new TextFlow(new Text("Tạo khiếu nại:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        VBox notificationInfo = new VBox();
        mainContent.getChildren().addAll(notificationInfo);
//        notificationInfo.setPrefHeight(mainContent.getPrefHeight() * 0.2);
//        VBox.setVgrow(notificationInfo, Priority.ALWAYS);
        notificationInfo.setMinWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setAlignment(Pos.CENTER_LEFT);
        notificationInfo.setPadding(new Insets(20, 50, 20, 50));
        notificationInfo.getStyleClass().add("public-profile");
        mainContent.setSpacing(20);
        notificationInfo.setSpacing(20);

        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge("Info", MaterialDesign.MDI_INFORMATION_OUTLINE, Color.valueOf("#0969da")));
        badges.add(new Badge("Success", MaterialDesign.MDI_CHECK_CIRCLE_OUTLINE, Color.valueOf("#1f823b")));
        badges.add(new Badge("Warning", MaterialDesign.MDI_ALERT_OUTLINE, Color.valueOf("#9a6801")));
        badges.add(new Badge("Danger", MaterialDesign.MDI_ALERT_CIRCLE_OUTLINE, Color.valueOf("#d2313c")));
        ComboBox<Badge> notiType = new ComboBox<>(FXCollections.observableArrayList(badges));
        notiType.setButtonCell(new BadgeCell()); // Set button appearance
        notiType.setCellFactory(c -> new BadgeCell()); // Set dropdown appearance
        notiType.getSelectionModel().selectFirst(); // Default selection
        notificationInfo.getChildren().add(new VerticleTextAndComboBox("Loại: ", notiType, "Enter the type of notification", "noti-type-info", true));
        notificationInfo.getChildren().add(new VerticleTextAndTextField("Tiêu đề Khiếu nại:", null, "enter the title of the notification", "noti-title-info", true));
        notificationInfo.getChildren().add(new VerticleTextAndTextArea("Nội dung khiếu nại: ", null, "enter the message of the notification", "noti-message-info", true));

        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));


        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);

        HBox buttonContainer = new HBox();
        Button save = new Button("Tạo khiếu nại");
        save.setId("save-button");
        buttonContainer.getChildren().add(save);
        mainContent.getChildren().add(buttonContainer);
        buttonContainer.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        buttonContainer.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        buttonContainer.setMinWidth(mainContent.getPrefWidth() * 0.9);
        save.setOnAction(event -> {
            String title = ((VerticleTextAndTextField) mainContent.lookup("#noti-title-info")).getTextField().getText();
            Validation vl = controller.titleCheck(title);
            if (vl.state() == ValidationState.ERROR) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }

            String message = ((VerticleTextAndTextArea) mainContent.lookup("#noti-message-info")).getTextArea().getText();
            vl = controller.messageCheck(message);
            if (vl.state() == ValidationState.ERROR) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            NotificationType type = NotificationType.valueOf(notiType.getValue().text());

            Feedback feedback = new Feedback(controller.getResident().getResidentId(), title, message, notiType.getValue().text(), false);
            feedbackService.save(feedback);
            reset();
            controller.reset();
            showPopUpMessage("Thành công", "Tạo khiếu nại thành công!");
        });
        return scene;
    }
}
