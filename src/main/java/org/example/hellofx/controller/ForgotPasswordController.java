package org.example.hellofx.controller;

import org.example.hellofx.model.Account;
import org.example.hellofx.model.Validation;
import org.example.hellofx.service.AccountService;
import org.example.hellofx.service.EmailService;
import org.example.hellofx.service.Generator;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.LoginScene;
import org.example.hellofx.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ForgotPasswordController {
    @Autowired
    AccountService accountService;
    @Autowired
    EmailService emailService;

    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    Generator generator;

    public void loginClicked() {
        JavaFxApplication.showThemeScene(LoginScene.class);
    }

    public String sendResetMail(String email) {
        String code = generator.generate6DigitCode();
        emailService.sendSimpleEmail(email, "Mã xác nhận lấy lại mật khẩu Bluemoon Resident Management", code + " là mã xác nhận của bạn");
        return code;
    }

    public void resetRequest(String email, String password) {
        Optional<Account> response = accountService.findByEmail(email);
        if (response.isPresent()) {
            Account account = response.get();
            account.setPasswordHash(password);
            for (int i = 0; i < 10; i++) {
                accountService.save(account);
            }
        }
    }

    public Validation emailCheck(String email) {
        return emailValidator.emailCheck(email);
    }
}
