package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class VerticleTextAndComboBox extends VBox {
    private Text text;
    private ComboBox comboBox;

    public Text getText() {
        return text;
    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    public VerticleTextAndComboBox(String text, ComboBox cmb, String prompt, String id, boolean editable) {
        super();
        if (id != null) {
            setId(id);
        }
        this.text = new Text(text);
        getChildren().add(this.text);
        this.comboBox = cmb;
        this.comboBox.setPromptText(prompt);
        this.comboBox.setEditable(false);
        getChildren().add(this.comboBox);
        setAlignment(Pos.TOP_LEFT);
//        this.comboBox.setPrefWidth(250);
        this.comboBox.setDisable(!editable);
        if (!editable) {
            this.comboBox.getStyleClass().add("not-editable-text-field");
        } else {
            this.comboBox.getStyleClass().add("editable-text-field");
        }
        getChildren().add(new Separator(Orientation.HORIZONTAL));
        setSpacing(0);
    }
}
