package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleBooleanProperty;
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
import org.example.hellofx.controller.ApartmentCreationController;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextComboBox;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ApartmentCreationScene extends Notificable implements ThemeScene {
    @Autowired
    private ApartmentCreationController controller;

    private Scene scene;

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<Resident> masterData;
    private TableView<Resident> table;
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
        TextFlow section = new TextFlow(new Text("Tạo căn hộ mới:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        VBox apartmentInfo = new VBox();
        mainContent.getChildren().addAll(apartmentInfo);
        apartmentInfo.setMinWidth(mainContent.getPrefWidth() * 0.9);
        apartmentInfo.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        apartmentInfo.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        apartmentInfo.setAlignment(Pos.CENTER_LEFT);
        apartmentInfo.setPadding(new Insets(20, 50, 20, 50));
        apartmentInfo.getStyleClass().add("public-profile");
        mainContent.setSpacing(20);
        apartmentInfo.setSpacing(20);

        apartmentInfo.getChildren().add(new VerticleTextAndTextField("Tên căn hộ:", null, "enter the name of the apartment", "apartment-name-info", true));

        HBox doubleTab0 = new HBox();
        doubleTab0.setPrefWidth(apartmentInfo.getPrefWidth());
        VBox leftTab0 = new VBox(), rightTab0 = new VBox();
        doubleTab0.setSpacing(doubleTab0.getPrefWidth() * 0.1);
        leftTab0.setPrefWidth(doubleTab0.getPrefWidth() * 0.4);
        rightTab0.setPrefWidth(doubleTab0.getPrefWidth() * 0.4);
        leftTab0.setAlignment(Pos.TOP_CENTER);
        rightTab0.setAlignment(Pos.TOP_CENTER);
        leftTab0.getChildren().add(new VerticleTextAndTextField("Tiền thuê nhà hàng tháng (vnđ): ", null, "enter the amount", "monthly-rent-price", true, true, false));
        doubleTab0.getChildren().addAll(leftTab0, rightTab0);

        HBox doubleTab = new HBox();
        apartmentInfo.getChildren().addAll(doubleTab0, doubleTab);
        doubleTab.setPrefWidth(apartmentInfo.getPrefWidth());
        VBox leftTab = new VBox(), rightTab = new VBox();
        doubleTab.getChildren().addAll(leftTab, rightTab);
        doubleTab.setSpacing(doubleTab.getPrefWidth() * 0.1);
        leftTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        rightTab.setPrefWidth(doubleTab.getPrefWidth() * 0.4);
        leftTab.setAlignment(Pos.TOP_CENTER);
        rightTab.setAlignment(Pos.TOP_CENTER);
        leftTab.getChildren().add(new VerticleTextAndTextField("Số điện của tháng gần nhất (vnđ): ", null, "enter the amount", "last-month-electric-index", true, true, true));
        rightTab.getChildren().add(new VerticleTextAndTextField("Giá mỗi số điện (vnđ): ", null, "enter the amount of money", "electric-unit-price", true, true));
        leftTab.getChildren().add(new VerticleTextAndTextField("Số nước của tháng gần nhất (vnđ): ", null, "enter the amount", "last-month-water-index", true, true, true));
        rightTab.getChildren().add(new VerticleTextAndTextField("Giá mỗi số nước (vnđ): ", null, "enter the amount of money", "water-unit-price", true, true));

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
        if (controller.getProfile().getRole() != AccountType.Resident) {
            TextComboBox<AccountType> role = new TextComboBox<AccountType>("Theo quyền: ", FXCollections.observableArrayList(AccountType.values()), false, 140, "roleFilter", true);
            role.getComboBox().setValue(AccountType.Resident);
            filter.getChildren().add(role);
            ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#roleFilter")).setOnAction(event -> {
                reloadTable(scene);
            });
            filter.getChildren().add(new Separator(Orientation.VERTICAL));
        }
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
        table.setPrefWidth(apartmentInfo.getPrefWidth());
        table.setMaxWidth(apartmentInfo.getPrefWidth());
        table.setMinWidth(apartmentInfo.getPrefWidth());
        mainContent.getChildren().addAll(table, pagination);

        HBox createButtonContainer = new HBox();
        Button savebutton = new Button("Tạo căn hộ");
        Button cancelButton = new Button("Hủy");
        savebutton.setId("save-button");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setId("close");
        createButtonContainer.getChildren().addAll(savebutton, cancelButton);
        createButtonContainer.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMinWidth(mainContent.getPrefWidth() * 0.9);
        savebutton.setStyle("-fx-background-color: #4abc96 !important;");
//        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        mainContent.getChildren().addAll(createButtonContainer);

        createButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        createButtonContainer.setSpacing(20);

        savebutton.setOnAction(event -> {
            String name = ((VerticleTextAndTextField) mainContent.lookup("#apartment-name-info")).getTextField().getText();
            Validation vl = controller.checkName(name);
            if (vl.state() == ValidationState.ERROR) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            BigDecimal electricUnitPrice = null;

            String tmp = ((VerticleTextAndTextField) mainContent.lookup("#electric-unit-price")).getTextField().getText();
            if (tmp != null) {
                electricUnitPrice = BigDecimal.valueOf(Double.valueOf(tmp));
            }
            else {
                showPopUpMessage("ERROR", "giá mỗi số điện không được để trống!");
                return;
            }

            Integer lastMonthElectricIndex = null;
            tmp = ((VerticleTextAndTextField) mainContent.lookup("#last-month-electric-index")).getTextField().getText();
            if (tmp != null) {
                lastMonthElectricIndex = Integer.valueOf(tmp);
            }
            else {
                showPopUpMessage("ERROR", "Số điện không được để trống!");
                return;
            }

            BigDecimal waterUnitPrice = null;

            tmp = ((VerticleTextAndTextField) mainContent.lookup("#water-unit-price")).getTextField().getText();
            if (tmp != null) {
                waterUnitPrice = BigDecimal.valueOf(Double.valueOf(tmp));
            }
            else {
                showPopUpMessage("ERROR", "giá mỗi số nước không được để trống!");
                return;
            }

            Integer lastMonthWaterIndex = null;
            tmp = ((VerticleTextAndTextField) mainContent.lookup("#last-month-water-index")).getTextField().getText();
            if (tmp != null) {
                lastMonthWaterIndex = Integer.valueOf(tmp);
            }
            else {
                showPopUpMessage("ERROR", "Số nước không được để trống!");
                return;
            }

            BigDecimal monthlyRentPrice = null;
            tmp = ((VerticleTextAndTextField) mainContent.lookup("#monthly-rent-price")).getTextField().getText();
            if (tmp != null) {
                monthlyRentPrice = BigDecimal.valueOf(Double.valueOf(tmp));
            }
            else {
                showPopUpMessage("ERROR", "Tiền nhà hàng tháng không được để trống!");
                return;
            }

            List<Integer> ds = new ArrayList<>();
            selectedMap.forEach((k, v) -> {
                if (v.getValue().equals("Thuộc căn hộ")) {
                    ds.add(k);
                }
            });
            controller.save(name, monthlyRentPrice, lastMonthElectricIndex, electricUnitPrice, lastMonthWaterIndex, waterUnitPrice, ds);

            cancelButton.fire();
        });
        return scene;
    }

    void reloadTable(Scene scene) {
        String condition = "";
        ComboBox<AccountType> roleFilter = ((ComboBox<AccountType>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#roleFilter"));
        TextField searchFilter = ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField();
        masterData = controller.getResidentsByFilters(null, roleFilter.getValue().toString(), searchFilter.getText());
        resetPagination();
    }


    private void createTable () {
        var selectAll = new CheckBox();

        selectedMapUpdater = new TreeMap<>();
        selectedMap = new TreeMap<>();
        var col0 = new TableColumn<Resident, Boolean>();
        col0.setGraphic(selectAll);
        col0.setSortable(false);
        col0.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getResidentId();
            return selectedMapUpdater.computeIfAbsent(id, k -> new SimpleBooleanProperty(false));
        });
        col0.setCellFactory(CheckBoxTableCell.forTableColumn(col0));
        col0.setEditable(true);
        col0.setPrefWidth(60);

        var col1 = new TableColumn<Resident, String>("Họ");
        col1.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getLastName())
        );

        var col2 = new TableColumn<Resident, String>("Tên");
        col2.setCellValueFactory(
                c -> {
                    if (c.getValue().getFirstName() == null) {
                        return null;
                    }
                    return new SimpleStringProperty(c.getValue().getFirstName());
                }
        );

        var col3 = new TableColumn<Resident, String>("Trạng thái");
        col3.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getResidentId();
            return selectedMap.computeIfAbsent(id, k -> new SimpleStringProperty("Không thuộc"));
        });

        if (table == null) {
            table = new TableView<Resident>();
            pagination = new Pagination();
            //        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            masterData = FXCollections.observableArrayList();
        }
        table.getColumns().setAll(col0, col1, col2, col3);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );
        table.getSelectionModel().selectFirst();
        table.setId("resident-table");
        table.setRowFactory(tv -> {
            TableRow<Resident> row = new TableRow<>();
            return row;
        });
//        Styles.toggleStyleClass(table, Styles.STRIPED);
        if (controller.getProfile().getRole() == AccountType.Admin || controller.getProfile().getRole() == AccountType.Client) {
            table.setEditable(true);
        }
        else {
            selectAll.setDisable(true);
        }
        selectAll.setOnAction(event -> {
            table.getItems().forEach(item -> {
                selectedMapUpdater.get(item.getResidentId()).set(selectAll.isSelected());
            });
        });
        ((Button) mainContent.lookup("#add-all-button")).setOnAction(event -> {
            table.getItems().forEach(item -> {
                if (selectedMapUpdater.get(item.getResidentId()).getValue()) {
                    selectedMap.get(item.getResidentId()).setValue("Thuộc căn hộ");
                    selectedMapUpdater.get(item.getResidentId()).setValue(false);
                }
            });
            selectAll.setSelected(false);
        });
        ((Button) mainContent.lookup("#rm-all-button")).setOnAction(event -> {
            table.getItems().forEach(item -> {
                if (selectedMapUpdater.get(item.getResidentId()).getValue()) {
                    selectedMap.get(item.getResidentId()).set("Không thuộc");
                    selectedMapUpdater.get(item.getResidentId()).setValue(false);
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
        ObservableList<Resident> pageData = FXCollections.observableArrayList(
                masterData.subList(fromIndex, toIndex));

        table.setItems(pageData);
    }
}
