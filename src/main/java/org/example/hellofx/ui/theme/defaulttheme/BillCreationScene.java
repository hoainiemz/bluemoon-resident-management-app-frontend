package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import org.example.hellofx.controller.BillCreationController;
import org.example.hellofx.dto.ApartmentCountDTO;
import org.example.hellofx.model.Bill;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class BillCreationScene extends Notificable implements ThemeScene {
    @Autowired
    private BillCreationController controller;

    private Scene scene;

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<ApartmentCountDTO> masterData;
    private TableView<ApartmentCountDTO> table;
    private Pagination pagination;
    private VBox mainContent;
    private ScrollPane scrollPane;
    private Map<Integer, SimpleBooleanProperty> selectedMapUpdater;
    private Map<Integer, SimpleStringProperty> selectedMap;

    protected Scene getCurrentScene() {
        return scene;
    }

    public void reset() {
        masterData = null;
        table = null;
        pagination = null;
        mainContent = null;
        selectedMapUpdater = null;
        selectedMap = null;
    }

    void reloadTable(Scene scene) {
        TextField searchFilter = ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField();

        TableView<ApartmentCountDTO> table = (TableView) scene.lookup("#resident-table");
        masterData = controller.getApartmentsAndResidentCount(searchFilter.getText());
        resetPagination();
    }


    public Scene getScene(Scene scene) {
        reset();
        this.scene = scene;
        Pane container = (Pane) scene.lookup("#container");
        StackPane content = (StackPane) scene.lookup("#content");
        content.getChildren().clear();
        mainContent = new VBox();

        scrollPane = new ScrollPane();
        scrollPane.setContent(mainContent);

        content.getChildren().addAll(scrollPane);
        mainContent.setPrefWidth(content.getPrefWidth());
        mainContent.setMinWidth(content.getPrefWidth());
        mainContent.setMaxWidth(content.getPrefWidth());
        mainContent.setMinHeight(content.getPrefHeight());

        scrollPane.setPrefWidth(content.getPrefWidth());
        scrollPane.setMinWidth(content.getPrefWidth());
        scrollPane.setMaxWidth(content.getPrefWidth());
        scrollPane.setPrefHeight(content.getPrefHeight());
        scrollPane.setMinHeight(content.getPrefHeight());
        scrollPane.setMaxHeight(content.getPrefHeight());

        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setId("main-content");
        TextFlow section = new TextFlow(new Text("Tạo khoản thu:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        VBox billinfo = new VBox();
        mainContent.getChildren().addAll(billinfo);
//        billinfo.setPrefHeight(mainContent.getPrefHeight() * 0.2);
//        VBox.setVgrow(billinfo, Priority.ALWAYS);
        billinfo.setMinWidth(mainContent.getPrefWidth() * 0.9);
        billinfo.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        billinfo.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        billinfo.setAlignment(Pos.CENTER_LEFT);
        billinfo.setPadding(new Insets(20, 50, 20, 50));
        billinfo.getStyleClass().add("public-profile");
        mainContent.setSpacing(20);
        billinfo.setSpacing(20);

        billinfo.getChildren().add(new VerticleTextAndTextField("Tên khoản thu:", null, "enter the name of the bill", "bill-name-info", true));
        billinfo.getChildren().add(new VerticleTextAndTextArea("Mô tả khoản thu: ", null, "enter the description of the bill", "bill-description-info", true));

        HBox doubleTab = new HBox();
        billinfo.getChildren().addAll(doubleTab);
        doubleTab.setPrefWidth(billinfo.getPrefWidth());
        VBox leftTab = new VBox(), rightTab = new VBox();
        doubleTab.getChildren().addAll(leftTab, rightTab);
        doubleTab.setSpacing(doubleTab.getPrefWidth() * 0.1);
        leftTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        rightTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        leftTab.setAlignment(Pos.TOP_CENTER);
        rightTab.setAlignment(Pos.TOP_CENTER);
        leftTab.getChildren().add(new VerticleTextAndTextField("Số tiền phải nộp (vnđ): ", null, "enter the amount of money", "amount-info", true, true));
        rightTab.getChildren().add(new VerticleTextAndTextField("Phí nộp muộn (vnđ): ", null, "enter the late fee", "late-fee-info", true, true));
        ComboBox<String> req = new ComboBox<>(FXCollections.observableArrayList("Bắt buộc", "Không bắt buộc"));
        leftTab.getChildren().add(new VerticleTextAndComboBox("Ràng buộc: ", req, null, "required-info", true));
        rightTab.getChildren().add(new VerticleTextAndDateTimePicker("Hạn nộp phí(yyyy-mm-dd hh:pp): ", null, null, "due-info", true, true));

        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        TextFlow section2 = new TextFlow(new Text("Đối tượng:"));
        section2.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section2);


        HBox filter = new HBox();
        filter.setId("filter");
        Text boLoc = new Text("BỘ LỌC");
        boLoc.setStyle("-fx-font-weight: bold;");
        filter.getChildren().addAll(boLoc, new Separator(Orientation.VERTICAL));
        mainContent.getChildren().add(filter);
        filter.setPrefHeight(container.getPrefHeight() * 0.03);
        filter.setAlignment(Pos.CENTER_LEFT);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);

        filter.getChildren().add(new TextAndTextField("Theo từ khóa: ", null, "Enter the search keyword", "searchFilter", true));

        ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField().setOnAction(event -> {
            reloadTable(scene);
        });

        HBox actionContainer = new HBox();
        actionContainer.setId("action-container");
        actionContainer.getChildren().addAll(new Button("Thêm"), new Button("Gỡ"));
        actionContainer.getChildren().get(0).getStyleClass().add("action-button");
        actionContainer.getChildren().get(0).setId("add-all-button");
        actionContainer.getChildren().get(0).setStyle("-fx-background-color: rgba(164,42,0,255);");
        actionContainer.getChildren().get(1).getStyleClass().add("action-button");
        actionContainer.getChildren().get(1).setStyle("-fx-background-color: rgba(55,95,173,255);");
        actionContainer.getChildren().get(1).setId("rm-all-button");
        actionContainer.setSpacing(20);
        actionContainer.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        actionContainer.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        actionContainer.setMinWidth(mainContent.getPrefWidth() * 0.9);

        mainContent.getChildren().addAll(actionContainer);

        createTable();
        reloadTable(scene);

        Styles.toggleStyleClass(table, Styles.STRIPED);
        table.setPrefWidth(billinfo.getPrefWidth());
        table.setMaxWidth(billinfo.getPrefWidth());
        table.setMinWidth(billinfo.getPrefWidth());
        mainContent.getChildren().addAll(table, pagination);

        HBox createButtonContainer = new HBox();
        Button savebutton = new Button("Tạo khoản thu");
        Button cancelButton = new Button("Hủy");
        savebutton.setId("save-button");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setId("close");
        createButtonContainer.getChildren().addAll(savebutton, cancelButton);
        createButtonContainer.setSpacing(20);
        createButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        createButtonContainer.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMinWidth(mainContent.getPrefWidth() * 0.9);
        savebutton.setStyle("-fx-background-color: #4abc96 !important;");
//        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        mainContent.getChildren().addAll(createButtonContainer);
        savebutton.setOnAction(actionEvent -> {
            String name = ((VerticleTextAndTextField) mainContent.lookup("#bill-name-info")).getTextField().getText();
            if (name == null) {
                showPopUpMessage("ERROR", "Tên khoản thu không được để trống!");
                return;
            }
            String description = ((VerticleTextAndTextArea) mainContent.lookup("#bill-description-info")).getTextArea().getText();
            Double amount = null;
            String tmp = ((VerticleTextAndTextField) mainContent.lookup("#amount-info")).getTextField().getText();
            if (tmp != null) {
                amount = Double.valueOf(tmp);
            }
            else {
                showPopUpMessage("ERROR", "Khoản tiền phải đóng không được để trống!");
                return;
            }
            tmp = ((VerticleTextAndTextField) mainContent.lookup("#late-fee-info")).getTextField().getText();
            Double lateFee = null;
            if (tmp != null) {
                lateFee = Double.valueOf(tmp);
            }
            else {
                showPopUpMessage("ERROR", "Phí nộp muộn không được để trống!");
                return;
            }
            tmp = (String) ((VerticleTextAndComboBox) mainContent.lookup("#required-info")).getComboBox().getValue();
            if (tmp == null) {
                showPopUpMessage("ERROR", "Mục ràng buộc không được để trống!");
                return;
            }
            Boolean required = tmp.equals("Bắt buộc");
            LocalDateTime dueDate = ((VerticleTextAndDateTimePicker) mainContent.lookup("#due-info")).getDateTimePicker().getDateTimeValue();
            if (dueDate == null) {
                showPopUpMessage("ERROR", "Hạn nộp phí không được để trống!");
                return;
            }
            Bill newBill = new Bill(null, amount, lateFee, dueDate, name, description, required);

            List<Integer> ds = new ArrayList<>();
            selectedMap.forEach((k, v) -> {
                if (v.getValue().equals("Phải đóng")) {
                    ds.add(k);
                }
            });
            controller.createButtonClicked(newBill, ds);
            cancelButton.fire();
//            controller.reset();
//            showPopUpMessage("Thành công", "Tạo khoản thu thành công!");
        });
        return scene;
    }

    private void createTable () {
        var selectAll = new CheckBox();

        selectedMapUpdater = new TreeMap<>();
        selectedMap = new TreeMap<>();

        var col0 = new TableColumn<ApartmentCountDTO, Boolean>();
        col0.setGraphic(selectAll);
        col0.setSortable(false);
        col0.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getApartmentId();
            return selectedMapUpdater.computeIfAbsent(id, k -> new SimpleBooleanProperty(false));
        });
        col0.setCellFactory(CheckBoxTableCell.forTableColumn(col0));
        col0.setEditable(true);

        var col1 = new TableColumn<ApartmentCountDTO, String>("Tên căn hộ");
        col1.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getApartmentName())
        );

        var col2 = new TableColumn<ApartmentCountDTO, Long>("Số thành viên");
        col2.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getResidentCount()));

        var col3 = new TableColumn<ApartmentCountDTO, String>("Trạng thái");
        col3.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getApartmentId();
            return selectedMap.computeIfAbsent(id, k -> new SimpleStringProperty("Không phải đóng"));
        });

        if (table == null) {
            table = new TableView<ApartmentCountDTO>();
            pagination = new Pagination();
            masterData = FXCollections.observableArrayList();
            table.setEditable(true);
        }
        table.getColumns().setAll(col0, col1, col2, col3);
        table.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        col0.setPrefWidth(table.getPrefWidth() * 0.1);
        col0.setMaxWidth(table.getPrefWidth() * 0.1);
        col1.setPrefWidth(table.getPrefWidth() * 0.3);
        col1.setMaxWidth(table.getPrefWidth() * 0.3);
        col2.setPrefWidth(table.getPrefWidth() * 0.3);
        col2.setMaxWidth(table.getPrefWidth() * 0.3);
        col3.setPrefWidth(table.getPrefWidth() * 0.2);
        col3.setMaxWidth(table.getPrefWidth() * 0.2);
        table.getSelectionModel().selectFirst();
        table.setId("resident-table");
        selectAll.setOnAction(event -> {
            table.getItems().forEach(item -> {
                selectedMapUpdater.get(item.getApartmentId()).set(selectAll.isSelected());
            });
        });
        ((Button) mainContent.lookup("#add-all-button")).setOnAction(event -> {
            table.getItems().forEach(item -> {
                if (selectedMapUpdater.get(item.getApartmentId()).getValue()) {
                    selectedMap.get(item.getApartmentId()).setValue("Phải đóng");
                    selectedMapUpdater.get(item.getApartmentId()).setValue(false);
                }
            });
            selectAll.setSelected(false);
        });
        ((Button) mainContent.lookup("#rm-all-button")).setOnAction(event -> {
            table.getItems().forEach(item -> {
                if (selectedMapUpdater.get(item.getApartmentId()).getValue()) {
                    selectedMap.get(item.getApartmentId()).set("Không phải đóng");
                    selectedMapUpdater.get(item.getApartmentId()).setValue(false);
                }
            });
            selectAll.setSelected(false);
        });
    }


    void resetPagination() {
        int pageCount = (masterData.size() / ITEMS_PER_PAGE) + ((masterData.size() % ITEMS_PER_PAGE != 0) ? 1 : 0);

        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public javafx.scene.Node call(Integer pageIndex) {
                updateTable(pageIndex);
                return new Label(); // This label is not used visually
            }
        });
    }

    private void updateTable(int pageIndex) {
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, masterData.size());

        // Create a sublist for the current page
        ObservableList<ApartmentCountDTO> pageData = FXCollections.observableArrayList(
                masterData.subList(fromIndex, toIndex));

        table.setItems(pageData);
    }
}
