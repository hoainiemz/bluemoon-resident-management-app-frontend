package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.theme.Styles;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.example.hellofx.controller.BillSchedulerController;
import org.example.hellofx.dto.BillSchedulerDTO;
import org.example.hellofx.model.Bill;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextComboBox;
import org.example.hellofx.utils.ScreenUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class BillSchedulerScene  extends Notificable implements ThemeScene {
    @Autowired
    private BillSchedulerController controller;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<BillSchedulerDTO> masterData;
    private TableView<BillSchedulerDTO> table;
    private Pagination pagination;
    private VBox mainContent;
    private Scene scene;
    private Stage popupStage;
    Map<Integer, SimpleBooleanProperty> selectedMap;

    protected Scene getCurrentScene() {
        return scene;
    }

    public void reset() {
        masterData = null;
        table = null;
        pagination = null;
        mainContent = null;
    }

    void reloadTable(Scene scene) {
        ComboBox<String> requireFilter = ((ComboBox<String>) scene.lookup("#require-filter"));
        TextField searchFilter = ((TextAndTextField) scene.lookup("#searchFilter")).getTextField();
        table.getItems().clear();
        int kt = 0;
        if (requireFilter.getValue().equals("Tất cả")) {
            kt = 0;
        }
        else {
            if (requireFilter.getValue().equals("Bắt buộc")) {
                kt = 1;
            }
            else {
                kt = -1;
            }
        }

        masterData = controller.getBills(kt, searchFilter.getText());;
        resetPagination();
    }

    public Scene getScene(Scene scene) {
        reset();
        this.scene = scene;
        HBox container = (HBox) scene.lookup("#container");
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
        HBox searchBar = new HBox(new TextFlow(new Text("Danh sách các khoản phí định kỳ:")));
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

        filter.getChildren().add(new TextComboBox<String>("Loại: ", FXCollections.observableArrayList("Tất cả", "Bắt buộc", "Không bắt buộc"), false, 200, "require-filter", false, "Tất cả"));
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextAndTextField("Theo từ khóa: ", null, "Enter the search keyword", "searchFilter", true));

        ((ComboBox<String>) scene.lookup("#require-filter")).setOnAction(event -> {
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

        selectedMap = new TreeMap<>();
        var col0 = new TableColumn<BillSchedulerDTO, Boolean>();
        col0.setGraphic(selectAll);
        col0.setSortable(false);
        col0.setCellValueFactory(celldata -> {
            Integer id = celldata.getValue().getId();
            return selectedMap.computeIfAbsent(id, k -> new SimpleBooleanProperty(false));
        });
        col0.setCellFactory(CheckBoxTableCell.forTableColumn(col0));
        col0.setEditable(true);
        col0.setPrefWidth(60);

        var col1 = new TableColumn<BillSchedulerDTO, String>("Thời điểm tạo(yyyy-MM-dd HH:mm)");

        col1.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getBill().getDueDate() != null ? c.getValue().getBill().getDueDate().format(formatter) : ""
                )
        );

        var col2 = new TableColumn<BillSchedulerDTO, String>("Loại");
        col2.setCellValueFactory(
                c -> {
                    if (c.getValue().getBill().getRequired().booleanValue()) {
                        return new SimpleStringProperty("Bắt buộc");
                    }
                    return new SimpleStringProperty("Không bắt buộc");
                }
        );

        var col3 = new TableColumn<BillSchedulerDTO, String>("Nội dung khoản thu");
        col3.setCellValueFactory(
                c -> {
                    return new SimpleStringProperty(c.getValue().getBill().getContent());
                }
        );

        var col4 = new TableColumn<BillSchedulerDTO, Double>("Số tiền(vnđ)");
        col4.setCellValueFactory(
                c -> {
                    if (c.getValue().getBill().getFee() == null) {
                        return null;
                    }
                    return new SimpleDoubleProperty(c.getValue().getBill().getFee()).asObject();
                }
        );

        var col5 = new TableColumn<BillSchedulerDTO, HBox>("Thao tác");
        col5.setCellValueFactory(
                c -> {
                    FontIcon pencilIcon = new FontIcon();
                    pencilIcon.getStyleClass().add("pencil-icon");

                    FontIcon trashIcon = new FontIcon();
                    trashIcon.getStyleClass().add("delete-icon");

                    Button btnEdit   = new Button("", pencilIcon);
                    Button btnDelete = new Button("", trashIcon);

                    btnEdit.getStyleClass().add("btn-edit");
                    btnDelete.getStyleClass().add("btn-delete");

                    HBox hbox = new HBox(5, btnEdit);

                    btnEdit.setOnAction(event -> {
                        showInfoPopup(JavaFxApplication.getCurrentStage(), c.getValue().getId());
                    });

                    if (controller.getProfile().getRole() != AccountType.Resident) {
                        hbox.getChildren().add(btnDelete);
                        btnDelete.setOnAction(e -> {
                            showFullscreenPopup(JavaFxApplication.getCurrentStage(), c.getValue().getId());
                        });
                    }

                    return new SimpleObjectProperty(hbox);
                }
        );

        if (table == null) {
            table = new TableView<BillSchedulerDTO>();
            pagination = new Pagination();
            //        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            masterData = FXCollections.observableArrayList();
        }
        table.getColumns().setAll(col0, col1, col2, col3, col4, col5);
        table.setPrefWidth(mainContent.getPrefWidth());
        table.setEditable(true);
        col1.setPrefWidth(table.getPrefWidth() * 0.2);
        col2.setPrefWidth(table.getPrefWidth() * 0.1);
        col3.setPrefWidth(table.getPrefWidth() * 0.55);
        col4.setPrefWidth(table.getPrefWidth() * 0.15);
        col1.setMaxWidth(table.getPrefWidth() * 0.2);
        col2.setMaxWidth(table.getPrefWidth() * 0.1);
        col3.setMaxWidth(table.getPrefWidth() * 0.55);
        col4.setMaxWidth(table.getPrefWidth() * 0.15);
        selectAll.setOnAction(event -> {
            table.getItems().forEach(item -> {
                selectedMap.get(item.getId()).set(selectAll.isSelected());
            });
        });
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
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
        ObservableList<BillSchedulerDTO> pageData = FXCollections.observableArrayList(
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
        Scene popupScene = controller.getBillInfoScene(new Scene(overlay), id);
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

    private void showFullscreenPopup(Stage ownerStage, Integer billId) {
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
        Label popupTitle = new Label("Xóa khoản thu");
        popupTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label popupMessage = new Label("Thao tác này sẽ xóa khoản thu được chọn.\nBạn có chắc muốn tiếp tục.");
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
            controller.deleteById(billId);
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
