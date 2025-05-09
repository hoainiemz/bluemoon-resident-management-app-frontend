package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TextComboBox<T> extends HBox {
    ComboBox<T> comboBox;

    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    public TextComboBox(String text, ObservableList<T> ls, boolean editable, double width, String id) {
        getStyleClass().add("filter-item");
        var cmb = new ComboBox<T>();
        cmb.setItems(ls);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(new Text(text), cmb);
        cmb.getStyleClass().add("filter-item-combobox");
        cmb.setPrefWidth(width);
        cmb.setId(id);
        cmb.setEditable(editable);
        TextField field = cmb.getEditor();
        field.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
        comboBox = cmb;
    }
    public TextComboBox(String text, ObservableList<T> ls, boolean editable, double width, String id, boolean disable) {
        getStyleClass().add("filter-item");
        var cmb = new ComboBox<T>();
        cmb.setItems(ls);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(new Text(text), cmb);
        cmb.getStyleClass().add("filter-item-combobox");
        cmb.setPrefWidth(width);
        cmb.setId(id);
        cmb.setEditable(editable);
        cmb.setDisable(disable);
        TextField field = cmb.getEditor();
        field.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
        comboBox = cmb;
    }
    public TextComboBox(String text, ObservableList<T> ls, boolean editable, double width, String id, boolean disable, T value) {
        this(text, ls, editable, width, id, disable);
        this.getComboBox().setValue(value);
    }

    public TextComboBox(String text, ComboBox<T> comboBox, boolean editable, String id, boolean disable) {

        getStyleClass().add("filter-item");
        this.comboBox = comboBox;
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(new Text(text), comboBox);
        comboBox.getStyleClass().add("filter-item-combobox");
        comboBox.setEditable(editable);
        comboBox.setId(id);
        comboBox.setDisable(disable);
        TextField field = comboBox.getEditor();
//        field.setStyle("-fx-background-color: transparent; -fx-border-radius: 10px;");
    }
}
