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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import org.example.hellofx.controller.NotificationCreationController;
import org.example.hellofx.model.NotificationItem;
import org.example.hellofx.model.Resident;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.model.enums.NotificationType;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.*;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class NotificationCreationScene extends Notificable implements ThemeScene {
    @Autowired
    private NotificationCreationController controller;

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

    void reset() {
        masterData = null;
        selectedMap = null;
        selectedMapUpdater = null;

        table = null;
        pagination = null;
        scrollPane = null;
        mainContent = null;
    }

    void reloadTable(Scene scene) {
        String condition = "";
        ComboBox<String> houseIdFilter = ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#houseIdFilter"));
        ComboBox<AccountType> roleFilter = ((ComboBox<AccountType>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#roleFilter"));
        TextField searchFilter = ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField();
        TableView<Resident> table = (TableView) scene.lookup("#resident-table");
        masterData = controller.getResidentsByFilters(houseIdFilter.getValue(), roleFilter.getValue().toString(), searchFilter.getText());
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
//        mainContent.setPrefHeight(content.getPrefHeight());
        mainContent.setMinHeight(content.getPrefHeight());
//        mainContent.setMaxHeight(content.getPrefHeight());

        scrollPane.setPrefWidth(content.getPrefWidth());
        scrollPane.setMinWidth(content.getPrefWidth());
        scrollPane.setMaxWidth(content.getPrefWidth());
        scrollPane.setPrefHeight(content.getPrefHeight());
        scrollPane.setMinHeight(content.getPrefHeight());
        scrollPane.setMaxHeight(content.getPrefHeight());

        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setId("main-content");
        TextFlow section = new TextFlow(new Text("Tạo thông báo:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));

        VBox notificationInfo = new VBox();
        mainContent.getChildren().addAll(notificationInfo);
//        notificationInfo.setPrefHeight(mainContent.getPrefHeight() * 0.2);
//        VBox.setVgrow(notificationInfo, Priority.ALWAYS);
        notificationInfo.setMinWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        notificationInfo.setAlignment(Pos.CENTER_LEFT);
        notificationInfo.setPadding(new Insets(20, 50, 20, 50));
        notificationInfo.getStyleClass().add("public-profile");
        mainContent.setSpacing(20);
        notificationInfo.setSpacing(20);

//        ComboBox<NotificationType> notiType = new ComboBox<>(FXCollections.observableArrayList(NotificationType.values()));
//        notiType.getSelectionModel().select(NotificationType.Info);
//        notiType.setEditable(false);
        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge("Info", MaterialDesign.MDI_INFORMATION_OUTLINE, Color.valueOf("#0969da")));
        badges.add(new Badge("Success", MaterialDesign.MDI_CHECK_CIRCLE_OUTLINE, Color.valueOf("#1f823b")));
        badges.add(new Badge("Warning", MaterialDesign.MDI_ALERT_OUTLINE, Color.valueOf("#9a6801")));
        badges.add(new Badge("Danger", MaterialDesign.MDI_ALERT_CIRCLE_OUTLINE, Color.valueOf("#d2313c")));
        ComboBox<Badge> notiType = new ComboBox<>(FXCollections.observableArrayList(badges));
        notiType.setButtonCell(new BadgeCell()); // Set button appearance
        notiType.setCellFactory(c -> new BadgeCell()); // Set dropdown appearance
        notiType.getSelectionModel().selectFirst(); // Default selection
        notificationInfo.getChildren().add(new VerticleTextAndComboBox("Loại: ", notiType, "Enter the type of notification", "noti-type-info", true));
        notificationInfo.getChildren().add(new VerticleTextAndTextField("Tiêu đề:", null, "enter the title of the notification", "noti-title-info", true));
        notificationInfo.getChildren().add(new VerticleTextAndTextArea("Nội dung thông báo: ", null, "enter the message of the notification", "noti-message-info", true));


        VBox scheduler = new VBox();
        Text schedulerText = new Text("Thông báo theo định kỳ:");
        scheduler.getChildren().add(schedulerText);
        scheduler.setAlignment(Pos.TOP_LEFT);
        schedulerText.setStyle(" -fx-font-size: 25px;" +
                "-fx-fill: black;");
        HBox radioContainer = new HBox();
        notificationInfo.getChildren().addAll(scheduler);
        var group = new ToggleGroup();
        var yesRadio = new RadioButton("Có");
        yesRadio.setToggleGroup(group);
        var noRadio = new RadioButton("Không");
        noRadio.setToggleGroup(group);
        radioContainer.getChildren().addAll(yesRadio, noRadio);
        radioContainer.setSpacing(40);
        radioContainer.setAlignment(Pos.CENTER_LEFT);
        scheduler.getChildren().addAll(radioContainer);
        noRadio.setSelected(true);
        HBox schedulerInfo = new HBox();
        schedulerInfo.getChildren().add(new VerticleTextAndDateTimePicker("Thời điểm tạo(yyyy-mm-dd hh:pp)", null, null, "schedule-start", true, false));
        ComboBox<String> schedulecmb = new ComboBox<>(FXCollections.observableArrayList("Năm", "Tháng", "Ngày", "Giờ", "Phút"));
        schedulerInfo.getChildren().add(new VerticleTextAndComboBox("Chu kỳ tạo thông báo", schedulecmb, null, "schedule-cycle", true));
        yesRadio.setOnAction(e -> {
            scheduler.getChildren().add(schedulerInfo);
        });
        noRadio.setOnAction(e -> {
            if (scheduler.getChildren().contains(schedulerInfo)) {
                scheduler.getChildren().remove(schedulerInfo);
            }
        });
        scheduler.setSpacing(20);
        schedulerInfo.setSpacing(20);

        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        TextFlow section2 = new TextFlow(new Text("Đối tượng được thông báo:"));
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

//        filter.getChildren().add(new TextComboBox<AccountType>("Theo trạng thái user: ", FXCollections.observableArrayList(AccountType.Admin, AccountType.Client, AccountType.Resident), false, 150, ""));
//        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextComboBox<String>("Theo phòng: ", controller.getAllHouseIds(), true, 100, "houseIdFilter"));
        if (controller.getProfile().getRole() != AccountType.Resident) {
            TextComboBox<AccountType> role = new TextComboBox<AccountType>("Theo quyền: ", FXCollections.observableArrayList(AccountType.values()), false, 140, "roleFilter", true);
            role.getComboBox().setValue(AccountType.Resident);
            filter.getChildren().add(new Separator(Orientation.VERTICAL));
            filter.getChildren().add(role);
            ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#roleFilter")).setOnAction(event -> {
                reloadTable(scene);
            });
        }
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextAndTextField("Theo từ khóa: ", null, "Enter the search keyword", "searchFilter", true));


        ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#houseIdFilter")).setOnAction(event -> {
            reloadTable(scene);
        });

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
        table.setPrefWidth(notificationInfo.getPrefWidth());
        table.setMaxWidth(notificationInfo.getPrefWidth());
        table.setMinWidth(notificationInfo.getPrefWidth());
        mainContent.getChildren().addAll(table, pagination);

        HBox createButtonContainer = new HBox();
        Button savebutton = new Button("Tạo thông báo");
        Button cancelButton = new Button("Hủy");
        savebutton.setId("save-button");
        cancelButton.getStyleClass().add("cancel-button");
        cancelButton.setId("close");
        createButtonContainer.getChildren().addAll(savebutton, cancelButton);
        createButtonContainer.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMaxWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setMinWidth(mainContent.getPrefWidth() * 0.9);
        createButtonContainer.setSpacing(20);
        createButtonContainer.setAlignment(Pos.CENTER_RIGHT);
        savebutton.setStyle("-fx-background-color: #4abc96 !important;");
        mainContent.getChildren().add(new Separator(Orientation.HORIZONTAL));
        mainContent.getChildren().addAll(createButtonContainer);
        savebutton.setOnAction(actionEvent -> {
            String title = ((VerticleTextAndTextField) mainContent.lookup("#noti-title-info")).getTextField().getText();
            Validation vl = controller.titleCheck(title);
            if (vl.state() == ValidationState.ERROR) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            String message = ((VerticleTextAndTextArea) mainContent.lookup("#noti-message-info")).getTextArea().getText();
            vl = controller.messageCheck(message);
            if (vl.state() == ValidationState.ERROR) {
                showPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            NotificationType type = NotificationType.valueOf(notiType.getValue().text());

            NotificationItem notification = new NotificationItem(title, message, notiType.getValue().text());

            List<Integer> ds = new ArrayList<>();
            selectedMap.forEach((k, v) -> {
                if (v.getValue().equals("Nhận")) {
                    ds.add(k);
                }
            });

            if (yesRadio.isSelected()) {
                VerticleTextAndDateTimePicker time = (VerticleTextAndDateTimePicker) mainContent.lookup("#schedule-start");
                VerticleTextAndComboBox cycle = (VerticleTextAndComboBox) mainContent.lookup("#schedule-cycle");
                Validation vl1 = controller.scheduleValidate(time.getDateTimePicker().getDateTimeValue(), (String) cycle.getComboBox().getValue());
                if (!vl1.state().equals(ValidationState.OK)) {
                    showPopUpMessage("ERROR", vl1.message());
                    return;
                }
                controller.createNotificationClicked(notification, ds, time.getDateTimePicker().getDateTimeValue(), (String) cycle.getComboBox().getValue());
            }
            else {
                controller.createNotificationClicked(notification, ds);
            }
            reset();
            cancelButton.fire();
//            controller.reset();
//            showPopUpMessage("Thành công", "Tạo thông báo thành công!");
        });
        return scene;
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

        var col3 = new TableColumn<Resident, String>("Thông báo");
        col3.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getResidentId();
            return selectedMap.computeIfAbsent(id, k -> new SimpleStringProperty("Không nhận"));
        });
//        col4.setCellFactory(CheckBoxTableCell.forTableColumn(col4));
//        col4.setPrefWidth(60);

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
                    selectedMap.get(item.getResidentId()).setValue("Nhận");
                    selectedMapUpdater.get(item.getResidentId()).setValue(false);
                }
            });
            selectAll.setSelected(false);
        });
        ((Button) mainContent.lookup("#rm-all-button")).setOnAction(event -> {
            table.getItems().forEach(item -> {
                if (selectedMapUpdater.get(item.getResidentId()).getValue()) {
                    selectedMap.get(item.getResidentId()).set("Không nhận");
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
