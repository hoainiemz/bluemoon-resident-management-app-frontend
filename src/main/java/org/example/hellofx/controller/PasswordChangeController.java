package org.example.hellofx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordChangeController{
    @Autowired
    private ProfileController profileController;

    public String passwordChangeRequest(String username, String password, String xacNhanMatKhauMoiPasswordField) {
        return profileController.passwordChangeRequest(username, password, xacNhanMatKhauMoiPasswordField);
    }
}
