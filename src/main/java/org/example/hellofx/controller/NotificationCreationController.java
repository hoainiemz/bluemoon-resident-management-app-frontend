package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.model.*;
import org.example.hellofx.service.NoticementService;
import org.example.hellofx.service.NotificationService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.NotificationCreationScene;
import org.example.hellofx.validator.NotificationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationCreationController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NoticementService noticementService;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private NotificationValidator notificationValidator;

    public Resident getResident() {
        return profileController.getResident();
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public void reset() {
        JavaFxApplication.showThemeScene(NotificationCreationScene.class);
    }

//    @Transactional
    public void createNotificationClicked(NotificationItem notification, List<Integer> ds) {
//        System.out.println(notification.getTitle() + "\n" + notification.getMessage() + "\n" + notification.getType() + "\n" + notification.getCreatedAt());
        int numTries = 10;
        NotificationItem noti = null;
        for (int i = 0; i < numTries; i++) {
            try {
                noti = notificationService.save(notification);
                break;
            }
            catch (Exception e) {
                continue;
            }
        }
        assert  noti != null;
        Integer notiId = noti.getId();
        List<Noticement> noticements = ds.stream()
                .map(d -> new Noticement(null, notiId, d, false))
                .collect(Collectors.toList());
        for (int i = 0; i < 10; i++) {
            try {
                noticementService.saveAll(noticements);
                return;
            }
            catch (Exception e) {
                continue;
            }
        }
    }
    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public ObservableList<String> getAllHouseIds(){
        return FXCollections.observableArrayList(residentService.findDistinctNonNullHouseId(getProfile(), getResident()));
    }

    public Validation titleCheck(String title) {
        return notificationValidator.titleCheck(title);
    }

    public Validation messageCheck(String message) {
        return notificationValidator.contentCheck(message);
    }
    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }
}
