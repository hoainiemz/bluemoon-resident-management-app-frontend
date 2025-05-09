package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.UnaryOperator;

public class VerticleTextAndTextField extends VBox {
    private Text text;
    private TextField textField;

    public Text getText() {
        return text;
    }

    public TextField getTextField() {
        return textField;
    }

    public VerticleTextAndTextField(String text, String value, String prompt, String id, boolean editable) {
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
        setAlignment(Pos.TOP_LEFT);
//        this.textField.setPrefWidth(250);
        if (!editable) {
            this.textField.getStyleClass().add("not-editable-text-field");
        } else {
            this.textField.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
    }

    public VerticleTextAndTextField(String text, String value, String prompt, String id, boolean editable, boolean numberOnly) {
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
        setAlignment(Pos.TOP_LEFT);
//        this.textField.setPrefWidth(250);
        if (!editable) {
            this.textField.getStyleClass().add("not-editable-text-field");
        } else {
            this.textField.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
        UnaryOperator<TextFormatter.Change> digitFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) { // Only digits allowed
                return change;
            }
            return null; // Reject the change
        };
        textField.setTextFormatter(new TextFormatter<>(digitFilter));
    }
    public VerticleTextAndTextField(String text, String value, String prompt, String id, boolean editable, boolean numberOnly, boolean integerOnly) {
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
        setAlignment(Pos.TOP_LEFT);
//        this.textField.setPrefWidth(250);
        if (!editable) {
            this.textField.getStyleClass().add("not-editable-text-field");
        } else {
            this.textField.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
        UnaryOperator<TextFormatter.Change> digitFilter = change -> {
            String newText = change.getControlNewText();
            if ((newText.matches("\\d*\\.?\\d*") && !integerOnly) || (integerOnly && newText.matches("\\d*"))) { // Only digits allowed
                return change;
            }
            return null; // Reject the change
        };
        textField.setTextFormatter(new TextFormatter<>(digitFilter));
    }
}
