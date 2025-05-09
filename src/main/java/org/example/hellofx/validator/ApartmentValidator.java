package org.example.hellofx.validator;

import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApartmentValidator {
    @Autowired
    ApartmentService apartmentService;

    public Validation checkName(String value) {
        if (value == null || value.isEmpty()) {
            return new Validation(ValidationState.ERROR, "Tên căn hộ không được để trống");
        }
        if (value.length() > 255) {
            return new Validation(ValidationState.ERROR, "Tên căn hộ có độ dài không quá 255 ký tự");
        }
        if (apartmentService.checkExistsByApartmentName(value)) {
            return new Validation(ValidationState.ERROR, "Đã có căn hộ trùng tên");
        }
        return new Validation(ValidationState.OK, "OK!");
    }
}
