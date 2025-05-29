package org.example.hellofx.validator;

import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.ValidationState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerValidator {
    public Validation validate(LocalDateTime localDateTime, String cycle) {
        if (localDateTime == null) {
            return new Validation(ValidationState.ERROR, "Thời điểm tạo không được để trống");
        }
        if (localDateTime.isBefore(LocalDateTime.now())) {
            return new Validation(ValidationState.ERROR, "Thời điểm tạo phải sau thời điểm hiện tại");
        }
        if (cycle == null || cycle.isEmpty()) {
            return new Validation(ValidationState.ERROR, "Chu kỳ tạo không được để trống");
        }
        return new Validation(ValidationState.OK, "OK!");
    }
}
