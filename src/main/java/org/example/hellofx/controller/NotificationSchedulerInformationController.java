package org.example.hellofx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.dto.NotificationSchedulerDTO;
import org.example.hellofx.model.*;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.service.SchedulerService;
import org.example.hellofx.validator.NotificationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationSchedulerInformationController {
    @Autowired
    private SchedulerService schedulingService;
    @Autowired
    private ProfileController profileController;
    @Autowired
    private ResidentService residentService;
    @Autowired
    private NotificationValidator notificationValidator;
    @Autowired
    private SchedulerService schedulerService;

    public Scheduler getSchedulerById(int id) {
        return schedulingService.getById(id);
    }

    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
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

    public void saveButtonClicked(int id, NotificationItem notification, List<Integer> ds, LocalDateTime time, String cycle) {
        notification.setCreatedAt(time);
        NotificationSchedulerDTO schedulerDTO = new NotificationSchedulerDTO(notification, ds);

        schedulerDTO.setId(id);

        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {json = objectMapper.writeValueAsString(schedulerDTO);
//            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Scheduler scheduler = new Scheduler("Notification", json, time, cycle);
        scheduler.setSchedulerId(Long.valueOf(id));
        schedulerService.save(scheduler);
    }
}
