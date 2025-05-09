package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VerticleTextAndDateTimePicker extends VBox {
    private Text text;
    private DatePicker datePicker;
    private DateTimePicker dateTimePicker;

    public Text getText() {
        return text;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public DateTimePicker getDateTimePicker() {
        return dateTimePicker;
    }

    public VerticleTextAndDateTimePicker(String text, LocalDate value, String prompt, String id, boolean editable) {
        super();
        if (id != null) {
            setId(id);
        }
        this.text = new Text(text);
        getChildren().add(this.text);
        this.datePicker = new DatePicker(value);
        this.datePicker.setPromptText(prompt);
        this.datePicker.setEditable(editable);
        getChildren().add(this.datePicker);
        setAlignment(Pos.TOP_LEFT);
//        this.datePicker.setPrefWidth(250);
        if (!editable) {
            this.datePicker.getStyleClass().add("not-editable-text-field");
        } else {
            this.datePicker.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
    }

    public VerticleTextAndDateTimePicker(String text, LocalDateTime value, String prompt, String id, boolean editable, boolean time) {
        super();
        assert time;
        if (id != null) {
            setId(id);
        }
        this.text = new Text(text);
        getChildren().add(this.text);
        this.dateTimePicker = new DateTimePicker();
        if (value != null) {
            this.dateTimePicker.setDateTimeValue(value);
        }
        this.dateTimePicker.setPromptText(prompt);
        this.dateTimePicker.setEditable(editable);
        getChildren().add(this.dateTimePicker);
        setAlignment(Pos.TOP_LEFT);
//        this.dateTimePicker.setPrefWidth(250);
        if (!editable) {
            this.dateTimePicker.getStyleClass().add("not-editable-text-field");
        } else {
            this.dateTimePicker.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
    }
}
