package org.example.hellofx.controller;


import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.ForgotPasswordScene;
import org.example.hellofx.ui.theme.defaulttheme.LoginScene;
import org.example.hellofx.validator.EmailValidator;
import org.example.hellofx.validator.PasswordValidator;
import org.example.hellofx.validator.PhoneValidator;
import org.example.hellofx.validator.UsernameValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SignUpController{

    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private UsernameValidator usernameValidator;
    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private PhoneValidator phoneValidator;
    @Autowired
    private AccountService accountService;

    public void backToSignInClicked() {
        JavaFxApplication.showThemeScene(LoginScene.class);
    }

    public List<String> signUpClicked(String username, String password, String email, String phone) {
        List<String> res = new ArrayList<String>();
        Validation ck1 = usernameValidator.usernameCheck(username), ck2 = passwordValidator.passwordCheck(password), ck3 = emailValidator.emailSignupCheck(email), ck4 = phoneValidator.phoneCheck(phone);
        res.add(ck1.message());
        res.add(ck2.message());
        res.add(ck3.message());
        res.add(ck4.message());
        if (ck1.state() == ValidationState.OK && ck2.state() == ValidationState.OK && ck3.state() == ValidationState.OK && ck4.state() == ValidationState.OK) {
            accountService.createAccount(username, password, email, phone);
        }
        return res;
    }

    public void forgotPasswordClicked() {
        JavaFxApplication.showThemeScene(ForgotPasswordScene.class);
    }
}
