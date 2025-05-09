package org.example.hellofx.controller;

import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.service.ResidentService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.HomeScene;
import org.example.hellofx.ui.theme.defaulttheme.LoginScene;
import org.example.hellofx.ui.theme.defaulttheme.ResidentScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileController{
    @Autowired
    private ResidentService residentService;
    @Autowired
    private AccountService accountService;

    private Account profile;
    private Resident resident;

    public void logInRequest (Account profile) {
        this.profile = profile;
        resident = residentService.findResidentByAccount(profile);
//        System.out.println("Logged in with profile: " + profile);
        JavaFxApplication.showThemeScene(HomeScene.class);
    }

    public String getProfileNameRequest() {
        if (profile != null) {
            return profile.getUsername();
        }
        return null;
    }

    public void logOutRequest(){
        this.profile = null;
        this.resident = null;
        ((ResidentScene) SpringBootFxApplication.context.getBean(ResidentScene.class)).reset();
//        System.out.println("Logged out succesfully!");
        JavaFxApplication.showThemeScene(LoginScene.class);
    }

    public String passwordChangeRequest(String currentPassword, String newPassword, String confirmPassword) {
        assert isLoggedIn();
        if (newPassword.equals(confirmPassword) == false) {
            return "New password do not match!";
        }
        if (getCurrentPassword().equals(currentPassword) == false) {
            return "Current password do not match!";
        }
        int cnt = accountService.passwordChangeQuery(profile.getUsername(), newPassword);
        if (cnt == 0) {
            return "Failed to change password, please try again later!";
        }
        profile.setPasswordHash(newPassword);
        return "Password changed successfully!";
    }

    public boolean isLoggedIn() {
        return profile != null;
    }

    private String getCurrentPassword() {
        return profile.getPasswordHash();
    }

    public Resident getResident() {
        return resident;
    }

    public void residentProfileUpdateRequest(Resident resident) {
        residentService.updateResident(resident);
        this.resident = residentService.findResidentByAccount(profile);
    }

    public Account getProfile() {
        return profile;
    }

    public void accountProfileUpdateRequest(Account account) {
        accountService.updateAccount(account);
        if (profile.getUserId().equals(account.getUserId())) {
            profile = accountService.findAccountByUserId(account.getUserId());
        }
    }

    public Account findAccountByUserId(int userId) {
        return accountService.findAccountByUserId(userId);
    }

    public Resident findResidentByUserId(int userId) {
        return residentService.findResidentByUserId(userId);
    }
}
