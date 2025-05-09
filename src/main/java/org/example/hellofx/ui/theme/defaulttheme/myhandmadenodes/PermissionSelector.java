package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.stream.Collectors;

public class PermissionSelector extends VBox {

    private ObservableList<String> permissions;
    private ListView<String> listView;
    private TextField searchField;
    private Button selectAllButton;
    private Button deselectAllButton;

    public PermissionSelector() {
        // Initialize permissions data
        permissions = FXCollections.observableArrayList(
                "admin | đăng nhập | Can add log entry",
                "admin | đăng nhập | Can change log entry",
                "auth | Nhóm | Can add group",
                "auth | Nhóm | Can change group",
                "auth | cho phép | Can add permission",
                "auth | thành viên | Can add user"
        );

        // Create ListView for permissions
        listView = new ListView<>(permissions);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Lọc (Search)");
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                filterPermissions(newValue)
        );

        // Select All button
        selectAllButton = new Button("Chọn tất cả");
        selectAllButton.setOnAction(e -> listView.getSelectionModel().selectAll());

        // Deselect All button
        deselectAllButton = new Button("Bỏ chọn tất cả");
        deselectAllButton.setOnAction(e -> listView.getSelectionModel().clearSelection());

        // Button layout
        HBox buttonBox = new HBox(10, selectAllButton, deselectAllButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));

        // Main layout
        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().addAll(searchField, listView, buttonBox);
    }

    // Method to filter permissions based on the search query
    private void filterPermissions(String query) {
        if (query.isEmpty()) {
            listView.setItems(permissions);
        } else {
            listView.setItems(permissions.stream()
                    .filter(item -> item.toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        }
    }

    // Get the selected permissions
    public ObservableList<String> getSelectedPermissions() {
        return listView.getSelectionModel().getSelectedItems();
    }
}
