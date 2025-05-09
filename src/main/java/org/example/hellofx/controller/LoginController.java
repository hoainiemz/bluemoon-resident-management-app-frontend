package org.example.hellofx.controller;

import org.example.hellofx.model.Account;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.ForgotPasswordScene;
import org.example.hellofx.ui.theme.defaulttheme.SignUpScene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;

@Component
//@Transactional
public class LoginController {
    @Autowired
    ProfileController profileController;

    @Autowired
    AccountService accountService;

//    @Transactional
    public String loginButtonClicked(String username, String password) {
        Account response = accountService.findAccountByUsernameAndPassword(username, password);
        if (response != null) {
            profileController.logInRequest(response);
        }
        return response != null ? response.toString() : null;
    }

    public void signUpButtonClicked() {
        JavaFxApplication.showThemeScene(SignUpScene.class);
    }

    public void forgotPasswordClicked() {
        JavaFxApplication.showThemeScene(ForgotPasswordScene.class);
    }
}
