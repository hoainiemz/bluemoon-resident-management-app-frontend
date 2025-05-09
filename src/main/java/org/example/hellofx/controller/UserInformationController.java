package org.example.hellofx.controller;

import org.example.hellofx.model.Validation;
import org.example.hellofx.validator.EmailValidator;
import org.example.hellofx.validator.IdentityCardValidator;
import org.example.hellofx.validator.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInformationController {
    @Autowired
    private ProfileController profileController;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PhoneValidator phoneValidator;
    @Autowired
    private IdentityCardValidator identityCardValidator;

    public Validation emailCheck(String email) {
        return emailValidator.emailCheck(email);
    }

    public Validation phoneCheck(String phone) {
        return phoneValidator.phoneCheck(phone);
    }

    public Validation identityCardCheck(String identityCard) {
        return identityCardValidator.identityCardCheck(identityCard);
    }
}
