package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.function.UnaryOperator;

public class TextAndTextField extends HBox {
    private Text text;
    private TextField textField;

    public TextField getTextField() {
        return textField;
    }

    public Text getText() {
        return text;
    }

    public TextAndTextField(String text, String value, String prompt, String id, boolean editable) {
        super();
        if (id != null) {
            setId(id);
        }
        this.text = new Text(text);
        getChildren().add(this.text);
        this.textField = new TextField(value);
        this.textField.setPromptText(prompt);
        this.textField.setEditable(editable);
        getChildren().add(this.textField);
        setAlignment(Pos.CENTER_LEFT);
        this.textField.setPrefWidth(250);
        if (!editable) {
            this.textField.getStyleClass().add("not-editable-text-field");
        } else {
            this.textField.getStyleClass().add("editable-text-field");
        }
    }


    public TextAndTextField(String text, String value, String prompt, String id, boolean editable, boolean numberOnly) {
        super();
        if (id != null) {
            setId(id);
        }
        this.text = new Text(text);
        getChildren().add(this.text);
        this.textField = new TextField(value);
        this.textField.setPromptText(prompt);
        this.textField.setEditable(editable);
        getChildren().add(this.textField);
        setAlignment(Pos.CENTER_LEFT);
        this.textField.setPrefWidth(250);
        if (!editable) {
            this.textField.getStyleClass().add("not-editable-text-field");
        } else {
            this.textField.getStyleClass().add("editable-text-field");
        }
        if (numberOnly) {
            UnaryOperator<TextFormatter.Change> integerFilter = change -> {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*")) { // Allow optional negative sign and digits only
                    return change;
                }
                return null; // Reject the change
            };
            textField.setTextFormatter(new TextFormatter<>(integerFilter));
        }
    }
}
