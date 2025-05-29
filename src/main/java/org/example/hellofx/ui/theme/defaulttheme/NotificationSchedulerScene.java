package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.theme.Styles;
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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.example.hellofx.controller.NotificationSchedulerController;
import org.example.hellofx.dto.NotificationSchedulerDTO;
import org.example.hellofx.model.NotificationItem;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.Badge;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.BadgeCell;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextComboBox;
import org.example.hellofx.utils.ScreenUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationSchedulerScene implements ThemeScene {
    @Autowired
    private NotificationSchedulerController controller;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<NotificationSchedulerDTO> masterData;
    private TableView<NotificationSchedulerDTO> table;
    private Pagination pagination;
    private VBox mainContent;
    private Scene scene;
    private Stage popupStage;


    public void reset() {
        masterData = null;
        table = null;
        pagination = null;
        mainContent = null;
    }

    void reloadTable(Scene scene) {
        ComboBox<Badge> typeFilter = ((ComboBox<Badge>) scene.lookup("#type-filter"));
        TextField searchFilter = ((TextAndTextField) scene.lookup("#searchFilter")).getTextField();
        table.getItems().clear();
        masterData = controller.getNotifications(typeFilter.getValue().text(), searchFilter.getText());
        resetPagination();
    }

    public Scene getScene(Scene scene) {
        reset();
        this.scene = scene;
        Pane container = (Pane) scene.lookup("#container");
        StackPane content = (StackPane) scene.lookup("#content");
        content.getChildren().clear();
        if (mainContent != null) {
            content.getChildren().add(mainContent);
            return scene;
        }
        content.setAlignment(Pos.TOP_CENTER);
        mainContent = new VBox();
        content.getChildren().addAll(mainContent);
        mainContent.setPrefWidth(content.getPrefWidth());
        mainContent.setMinWidth(content.getPrefWidth());
        mainContent.setMaxWidth(content.getPrefWidth());
        mainContent.setPrefHeight(content.getPrefHeight());
        mainContent.setMinHeight(content.getPrefHeight());
        mainContent.setMaxHeight(content.getPrefHeight());

        mainContent.setAlignment(Pos.TOP_CENTER);

        mainContent.getChildren().clear();
        mainContent.getStyleClass().clear();
        mainContent.getStyleClass().add("doi-mat-khau");
        HBox searchBar = new HBox(new TextFlow(new Text("Danh sách các thông báo:")));
        searchBar.getStyleClass().add("big-text");
        mainContent.setPadding(new Insets(20, 50, 10, 50));
//        HBox mainContent = new HBox();
        searchBar.setMaxHeight(container.getPrefHeight() * 0.1);
        mainContent.getChildren().addAll(searchBar);
        ((TextFlow) searchBar.getChildren().get(0)).setPrefWidth(mainContent.getPrefWidth() * 0.7);

        HBox filter = new HBox();
        filter.setId("filter");
        Text boLoc = new Text("Bộ lọc:");
        boLoc.setStyle("-fx-font-weight: bold;");
        filter.getChildren().addAll(boLoc, new Separator(Orientation.VERTICAL));
        mainContent.getChildren().add(filter);
        filter.setPrefHeight(container.getPrefHeight() * 0.03);
        filter.setAlignment(Pos.CENTER_LEFT);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);

        List<Badge> badges = new ArrayList<>();
        badges.add(new Badge("All", MaterialDesign.MDI_FLAG_OUTLINE, Color.BLACK));
        badges.add(new Badge("Info", MaterialDesign.MDI_INFORMATION_OUTLINE, Color.valueOf("#0969da")));
        badges.add(new Badge("Success", MaterialDesign.MDI_CHECK_CIRCLE_OUTLINE, Color.valueOf("#1f823b")));
        badges.add(new Badge("Warning", MaterialDesign.MDI_ALERT_OUTLINE, Color.valueOf("#9a6801")));
        badges.add(new Badge("Danger", MaterialDesign.MDI_ALERT_CIRCLE_OUTLINE, Color.valueOf("#d2313c")));
        ComboBox<Badge> notiType = new ComboBox<>(FXCollections.observableArrayList(badges));
        notiType.setButtonCell(new BadgeCell()); // Set button appearance
        notiType.setCellFactory(c -> new BadgeCell()); // Set dropdown appearance
        notiType.getSelectionModel().selectFirst(); // Default selection
        filter.getChildren().add(new TextComboBox<Badge>("Loại: ", notiType, false, "type-filter", false));
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextAndTextField("Theo từ khóa: ", "", "Enter the search keyword", "searchFilter", true));

        ((ComboBox<String>) scene.lookup("#type-filter")).setOnAction(event -> {
            reloadTable(scene);
        });

        ((TextAndTextField) scene.lookup("#searchFilter")).getTextField().setOnAction(event -> {
            reloadTable(scene);
        });

        createTable();
        reloadTable(scene);
//
        mainContent.getChildren().addAll(table, pagination);

        return scene;
    }

    private void createTable () {
        var selectAll = new CheckBox();

        var col0 = new TableColumn<NotificationSchedulerDTO, String>("Thời điểm tạo(yyyy-MM-dd HH:mm)");

        col0.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getNotificationItem().getCreatedAt() != null ? c.getValue().getNotificationItem().getCreatedAt().format(formatter) : ""
                )
        );


        var col1 = new TableColumn<NotificationSchedulerDTO, String>("Loại");
        col1.setCellValueFactory(
                c -> {
                    return new SimpleStringProperty(c.getValue().getNotificationItem().getType());
                }
        );

        var col2 = new TableColumn<NotificationSchedulerDTO, String>("Tiêu đề");
        col2.setCellValueFactory(
                c -> {
                    return new SimpleStringProperty(c.getValue().getNotificationItem().getTitle());
                }
        );

        var col3 = new TableColumn<NotificationSchedulerDTO, String>("Nội dung");
        col3.setCellValueFactory(
                c -> {
                    if (c.getValue().getNotificationItem().getMessage() == null) {
                        return null;
                    }
                    return new SimpleStringProperty(c.getValue().getNotificationItem().getMessage());
                }
        );

        var col4 = new TableColumn<NotificationSchedulerDTO, HBox>("Thao tác");
        col4.setCellValueFactory(
                c -> {
                    FontIcon pencilIcon = new FontIcon();
                    pencilIcon.getStyleClass().add("pencil-icon");

                    FontIcon trashIcon = new FontIcon();
                    trashIcon.getStyleClass().add("delete-icon");

                    Button btnEdit   = new Button("", pencilIcon);
                    Button btnDelete = new Button("", trashIcon);

                    btnEdit.getStyleClass().add("btn-edit");
                    btnDelete.getStyleClass().add("btn-delete");

                    HBox hbox = new HBox(5, btnEdit, btnDelete);

                    btnEdit.setOnAction(event -> {
                        showInfoPopup(JavaFxApplication.getCurrentStage(), c.getValue().getId());
                    });

                    btnDelete.setOnAction(event -> {
                        showFullscreenPopup(JavaFxApplication.getCurrentStage(), c.getValue().getId());
                    });

                    return new SimpleObjectProperty(hbox);
                }
        );

        if (table == null) {
            table = new TableView<NotificationSchedulerDTO>();
            pagination = new Pagination();
            masterData = FXCollections.observableArrayList();
        }
        table.getColumns().setAll(col0, col1, col2, col3, col4);
        table.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        col0.setPrefWidth(table.getPrefWidth() * 0.25);
        col1.setPrefWidth(table.getPrefWidth() * 0.1);
        col2.setPrefWidth(table.getPrefWidth() * 0.3);
        col3.setPrefWidth(table.getPrefWidth() * 0.25);
        col0.setMaxWidth(table.getPrefWidth() * 0.25);
        col1.setMaxWidth(table.getPrefWidth() * 0.1);
        col2.setMaxWidth(table.getPrefWidth() * 0.3);
        col3.setMaxWidth(table.getPrefWidth() * 0.25);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
        Styles.toggleStyleClass(table, Styles.STRIPED);
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
        ObservableList<NotificationSchedulerDTO> pageData = FXCollections.observableArrayList(
                masterData.subList(fromIndex, toIndex));

        table.setItems(pageData);
    }

    private void showInfoPopup(Stage ownerStage, Integer id) {
        // Tạo một stage mới cho popup
        popupStage = new Stage();

        // Áp dụng hiệu ứng blur cho nội dung chính
        GaussianBlur blur = new GaussianBlur(10); // Độ mờ có thể điều chỉnh
        mainContent.setEffect(blur);

        // Panel chứa nội dung popup
        StackPane popupContent = new StackPane();
        popupContent.setPrefWidth(ScreenUtils.getScreenWidth() * 0.8);
        popupContent.setPrefHeight(ScreenUtils.getScreenHeight() * 0.8);
        popupContent.setAlignment(Pos.TOP_CENTER);
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0.0, 0.0);");
        popupContent.setId("content");

        // Lớp overlay bao phủ toàn màn hình
        AnchorPane overlay = new AnchorPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Màu đen với độ trong suốt 50%
        overlay.getChildren().add(popupContent);


        popupContent.translateXProperty().bind(
                overlay.widthProperty()
                        .subtract(popupContent.widthProperty())
                        .divide(2)
        );
        popupContent.translateYProperty().bind(
                overlay.heightProperty()
                        .subtract(popupContent.heightProperty())
                        .divide(2)
        );

        // Scene cho popup
        overlay.setId("container");
        Scene popupScene = controller.getNotificationInfoScene(new Scene(overlay), id);
        popupScene.setFill(Color.TRANSPARENT);

        String popupCssPath = getClass().getResource("/themes/default-theme/home/home.css").toExternalForm();
        popupScene.getStylesheets().add(popupCssPath);

        // Cấu hình stage cho popup
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ chính
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setScene(popupScene);

        // Đảm bảo popup có kích thước giống với cửa sổ chính
        popupStage.setX(ownerStage.getX());
        popupStage.setY(ownerStage.getY());
        popupStage.setWidth(ownerStage.getWidth());
        popupStage.setHeight(ownerStage.getHeight());

        // Xử lý sự kiện đóng popup
        ((Button) ((ScrollPane) overlay.lookup("ScrollPane")).getContent().lookup("#close")).setOnAction(e -> {
            popupStage.close();
            reloadTable(scene);
            mainContent.setEffect(null); // Xóa hiệu ứng blur khi đóng popup
        });

        // Xử lý khi đóng popup bằng cách khác (X, Alt+F4)
        popupStage.setOnCloseRequest(e -> {
            mainContent.setEffect(null);
        });

        overlay.requestFocus();
        // Hiển thị popup
        popupStage.show();
    }


    private void showFullscreenPopup(Stage ownerStage, Integer id) {
        // Tạo một stage mới cho popup
        Stage popupStage = new Stage();

        // Áp dụng hiệu ứng blur cho nội dung chính
        GaussianBlur blur = new GaussianBlur(10); // Độ mờ có thể điều chỉnh
        mainContent.setEffect(blur);

        // Panel chứa nội dung popup
        VBox popupContent = new VBox(20);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setMaxWidth(400);
        popupContent.setMaxHeight(300);
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0.0, 0.0);");

        // Nội dung của popup
        Label popupTitle = new Label("Xóa thông báo");
        popupTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label popupMessage = new Label("Thao tác này sẽ thông báo được chọn.\nBạn có chắc muốn tiếp tục.");
        popupMessage.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
        popupMessage.setWrapText(true);

        HBox popupButtons = new HBox(50);
        Button next = new Button("Có");
        Button quit = new Button("Không");
        next.getStyleClass().add("auto-addnew-button");
        quit.getStyleClass().add("auto-no-button");
        popupButtons.setAlignment(Pos.CENTER);
        popupButtons.getChildren().addAll(next, quit);

        popupContent.getChildren().addAll(popupTitle, popupMessage, popupButtons);

        next.setOnAction(e -> {
            controller.deleteById(id);
            quit.fire();
            reloadTable(scene);
        });

        // Lớp overlay bao phủ toàn màn hình
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Màu đen với độ trong suốt 50%
        overlay.getChildren().add(popupContent);

        // Scene cho popup
        Scene popupScene = new Scene(overlay);
        popupScene.setFill(Color.TRANSPARENT);
        String popupCssPath = getClass().getResource("/themes/default-theme/home/home.css").toExternalForm();
        popupScene.getStylesheets().add(popupCssPath);

        // Cấu hình stage cho popup
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ chính
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.setScene(popupScene);

        // Đảm bảo popup có kích thước giống với cửa sổ chính
        popupStage.setX(ownerStage.getX());
        popupStage.setY(ownerStage.getY());
        popupStage.setWidth(ownerStage.getWidth());
        popupStage.setHeight(ownerStage.getHeight());

        // Xử lý sự kiện đóng popup
        quit.setOnAction(e -> {
            popupStage.close();
            mainContent.setEffect(null); // Xóa hiệu ứng blur khi đóng popup
        });

        // Xử lý khi đóng popup bằng cách khác (X, Alt+F4)
        popupStage.setOnCloseRequest(e -> {
            mainContent.setEffect(null);
        });

        // Hiển thị popup
        popupStage.show();
    }
}
