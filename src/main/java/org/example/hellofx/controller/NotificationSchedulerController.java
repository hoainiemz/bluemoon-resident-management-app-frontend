package org.example.hellofx.controller;

import com.almasb.fxgl.notification.Notification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.dto.NotificationSchedulerDTO;
import org.example.hellofx.model.Scheduler;
import org.example.hellofx.service.SchedulerService;
import org.example.hellofx.ui.theme.defaulttheme.NotificationSchedulerInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSchedulerController {
    @Autowired
    private SchedulerService schedulerService;

    public ObservableList<NotificationSchedulerDTO> getNotifications(String typeFilter, String searchFilter) {
        List<Scheduler> lst = schedulerService.getNotificationByFilter(typeFilter, searchFilter);
        List<NotificationSchedulerDTO> dtos = lst.stream().map(x -> x.notificationDTO()).toList();
        return FXCollections.observableArrayList(dtos);
    }

    public Scene getNotificationInfoScene(Scene scene, Integer id ) {
        NotificationSchedulerInformationScene theme = SpringBootFxApplication.context.getBean(NotificationSchedulerInformationScene.class);
        return theme.getScene(id, scene);
    }

    public void deleteById(int id) {
        schedulerService.deleteById(id);
    }
}
