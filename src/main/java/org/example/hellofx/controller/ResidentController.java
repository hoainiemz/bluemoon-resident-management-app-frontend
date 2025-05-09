package org.example.hellofx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.UserInformationScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResidentController {
    @Autowired
    private ProfileController profileController;

    @Autowired
    private ResidentService residentService;
    @Autowired
    private AccountService accountService;

    public void seeMoreInformation(int id) {
        Account profile = accountService.findAccountByUserId(id);
        Resident resident = residentService.findResidentByUserId(id);
        JavaFxApplication.showUserInformationScene(profile, resident);
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public ObservableList<Resident> residentQuery(String query) {
        return FXCollections.observableArrayList(residentService.nativeResidentQuery(query));
    }

    public ObservableList<String> getAllHouseIds(){
        try {
            List<String> houseIds = residentService.findDistinctNonNullHouseId(getProfile(), getResident());
            return FXCollections.observableArrayList(houseIds);
        } catch (Exception e) {
            // Log the error and return empty list instead of propagating the exception
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }


    public ObservableList<Resident> getResidentsByFilters(String houseNameFilter, String roleFilter, String searchFilter) {
        return FXCollections.observableArrayList(residentService.findResidentsByFilters(houseNameFilter, roleFilter, searchFilter));
    }

    public Scene getResidentInfoScene(Scene scene, Integer residentId) {
        UserInformationScene theme = SpringBootFxApplication.context.getBean(UserInformationScene.class);
        Account profile = accountService.findAccountByUserId(residentId);
        Resident resident = residentService.findResidentByUserId(residentId);
        return theme.getScene(profile, resident, scene);
    }

    public void deleteResidentByResidentId(Integer residentId) {
        residentService.deleteResidentById(residentId);
    }
}
