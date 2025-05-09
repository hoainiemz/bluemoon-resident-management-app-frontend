package org.example.hellofx.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllResidentRequestController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private ResidentService residentService;
    @Autowired
    private AccountService accountService;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public void acceptButtonClicked(Resident newResident) {
        residentService.save(newResident);
    }

    public ObservableList<Account> accountsQuery(String query) {
        return FXCollections.observableArrayList(accountService.nativeAccountQuery(query));
    }
}
