package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Scheduler;
import org.example.hellofx.service.SchedulerService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.BillInformationScene;
import org.example.hellofx.ui.theme.defaulttheme.BillSchedulerInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillSchedulerController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private SchedulerService schedulerService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public ObservableList<BillSchedulerDTO> getBills(int requireFilter, String searchFilter) {
        List<Scheduler> lst = schedulerService.getBillByFilter(requireFilter, searchFilter);
        List<BillSchedulerDTO> dtos = lst.stream().map(x -> x.billDTO()).toList();
        return FXCollections.observableArrayList(dtos);
    }

    public Scene getBillInfoScene(Scene scene, Integer id) {
        BillSchedulerInformationScene theme = SpringBootFxApplication.context.getBean(BillSchedulerInformationScene.class);
        return theme.getScene(id, scene);
    }

    public void deleteById(int id) {
        schedulerService.deleteById(id);
    }
}
