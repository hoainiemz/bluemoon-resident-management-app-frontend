package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimePicker extends DatePicker {
    private final SimpleObjectProperty<LocalDateTime> dateTimeValue;
    private final DateTimeFormatter formatter;

    public DateTimePicker() {
        this(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public DateTimePicker(DateTimeFormatter formatter) {
        this.formatter = formatter;
        this.dateTimeValue = new SimpleObjectProperty<>();

        setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate value) {
                if (getDateTimeValue() != null) {
                    return getDateTimeValue().format(formatter);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String value) {
                if (value == null || value.trim().isEmpty()) {
                    dateTimeValue.set(null);
                    return null;
                }
                try {
                    dateTimeValue.set(LocalDateTime.parse(value, formatter));
                    return dateTimeValue.get().toLocalDate();
                } catch (Exception e) {
                    // If parsing fails, return current date value and don't update dateTimeValue
                    return getValue();
                }
            }

        });

        // Synchronize changes to the underlying date value back to the dateTimeValue
        valueProperty().addListener((observable, old, newValue) -> {
            if (newValue == null) {
                dateTimeValue.set(null);
            } else {
                if (dateTimeValue.get() == null) {
                    dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()));
                } else {
                    LocalTime time = dateTimeValue.get().toLocalTime();
                    dateTimeValue.set(LocalDateTime.of(newValue, time));
                }
            }
        });

        // Synchronize changes to dateTimeValue back to the underlying date value
        dateTimeValue.addListener((observable, old, newValue) -> {
            valueProperty().set(newValue != null ? newValue.toLocalDate() : null);
        });

        // Persist changes onblur
        getEditor().focusedProperty().addListener((observable, old, newValue) -> {
            if (!newValue) {
                simulateEnterPressed();
            }
        });
    }

    private void simulateEnterPressed() {
        getEditor().fireEvent(
                new KeyEvent(getEditor(), getEditor(), KeyEvent.KEY_PRESSED,
                        null, null, KeyCode.ENTER, false, false, false, false)
        );
    }

    public SimpleObjectProperty<LocalDateTime> dateTimeValueProperty() {
        return dateTimeValue;
    }

    public LocalDateTime getDateTimeValue() {
        return dateTimeValue.get();
    }

    public void setDateTimeValue(LocalDateTime dateTime) {
        dateTimeValue.set(dateTime);
    }

    public String getDateTimeValueString() {
        return dateTimeValue.get().format(formatter);
    }
}