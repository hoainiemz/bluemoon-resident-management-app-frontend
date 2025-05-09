package org.example.hellofx.controller;

import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Validation;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.FeedbackCreationScene;
import org.example.hellofx.validator.FeedbackValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackCreationController {
    @Autowired
    private FeedbackValidator feedbackValidator;
    @Autowired
    private ProfileController profileController;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public Validation titleCheck(String title) {
        return feedbackValidator.titleCheck(title);
    }
    public Validation messageCheck(String message) {
        return feedbackValidator.contentCheck(message);
    }

    public void reset() {
        JavaFxApplication.showThemeScene(FeedbackCreationScene.class);
    }
}
