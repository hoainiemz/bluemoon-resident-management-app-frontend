package org.example.hellofx.validator;

import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleValidator {
    @Autowired
    private VehicleService vehicleService;

    public Validation roomCheck(String room, List<String> rooms) {
        if (room != null && rooms.contains(room)) {
            return new Validation(ValidationState.OK, "OK");
        }
        return new Validation(ValidationState.ERROR, "Phòng bạn nhập không tồn tại");
    }

    public Validation licensePlateCheck(String val) {
        if (val == null || val.isEmpty()) {
            return new Validation(ValidationState.ERROR, "Biển số xe không được để trống");
        }
        if (vehicleService.checkExistByLicensePlate(val)) {
            return new Validation(ValidationState.ERROR, "Biển số xe đã được đăng ký");
        }
        return new Validation(ValidationState.OK, "OK");
    }
}
