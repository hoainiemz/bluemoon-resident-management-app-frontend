package org.example.hellofx.validator;

import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdentityCardValidator {
    @Autowired
    ResidentService residentService;

    public Validation identityCardCheck(String value) {
        if (value == null || value.isEmpty()) {
            return new Validation(ValidationState.ERROR, "Căn cước công dân không được bỏ trống!");
        }
        if (value.length() > 255) {
            return new Validation(ValidationState.ERROR, "Căn cước công dân có độ dài không được quá 255 ký tự!");
        }
        if (residentService.checkResidentExistByIdentityCard(value)) {
            return new Validation( ValidationState.ERROR, "Căn cước công dân bị trùng!");
        }
        return new Validation(ValidationState.OK, "OK!");
    }
}
