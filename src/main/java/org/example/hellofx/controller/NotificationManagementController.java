package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.model.NotificationItem;
import org.example.hellofx.service.NotificationService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.NotificationCreationScene;
import org.example.hellofx.ui.theme.defaulttheme.NotificationInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationManagementController{
    @Autowired
    NotificationService notificationService;

    public void seeNotificationItemInformation(Integer notiId) {
        JavaFxApplication.showNotificationInformationScene(notiId);
    }

    public ObservableList<NotificationItem> getNotifications(String typeFilter, String searchFilter) {
        return FXCollections.observableArrayList(notificationService.findNotifications(typeFilter, searchFilter));
    }

    public Scene getNotificationCreationScene(Scene scene) {
        return SpringBootFxApplication.context.getBean(NotificationCreationScene.class).getScene(scene);
    }

    public Scene getNotificationInfoScene(Scene scene, Integer notiId) {
        NotificationInformationScene theme = SpringBootFxApplication.context.getBean(NotificationInformationScene.class);
        return theme.getScene(notiId, scene);
    }

    public void deleteNotificationByNotificationId(Integer notiId) {
        notificationService.deleteNotificationById(notiId);
    }
}
