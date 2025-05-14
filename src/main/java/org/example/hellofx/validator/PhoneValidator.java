package org.example.hellofx.validator;

import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneValidator {
    @Autowired
    AccountService accountService;

    public Validation phoneCheck(String value) {
        if (value == null || value.isEmpty()) {
            return new Validation(ValidationState.ERROR, "Số điện thoại không được để trống!");
        }
        if (value.length() > 255) {
            return new Validation(ValidationState.ERROR, "Số điện thoại có độ dài không được quá 255 ký tự!");
        }
        if (accountService.checkAccountExistByPhone(value)) {
            return new Validation(ValidationState.ERROR, "Số điện thoại đã được sử dụng cho tài khoản khác!");
        }
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) < '0' || value.charAt(i) > '9') {
                return new Validation(ValidationState.ERROR, "Số điện thoại không đúng định dạng");
            }
        }
        return new Validation(ValidationState.OK, "OK!");
    }
}
