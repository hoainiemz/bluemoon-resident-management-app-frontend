package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.theme.Styles;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import org.example.hellofx.controller.BillResidentController;
import org.example.hellofx.dto.PaymentProjectionDTO;
import org.example.hellofx.model.InvoiceItem;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextComboBox;
import org.example.hellofx.utils.QRUtil;
import org.example.hellofx.utils.ScreenUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Component
public class BillResidentScene implements ThemeScene {
    @Autowired
    private BillResidentController controller;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.##"); // ví dụ: 4.095.000.000

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<PaymentProjectionDTO> masterData;
    private TableView<PaymentProjectionDTO> table;
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
        ComboBox<String> stateFilter = ((ComboBox<String>) scene.lookup("#state-filter"));
        ComboBox<String> requireFilter = ((ComboBox<String>) scene.lookup("#require-filter"));
        ComboBox<String> dueFilter = ((ComboBox<String>) scene.lookup("#due-filter"));
        TextField searchFilter = ((TextAndTextField) scene.lookup("#searchFilter")).getTextField();
        table.getItems().clear();
        int kt1 = 0, kt2 = 0, kt3 = 0;
        if (stateFilter.getValue().equals("Tất cả")) {
            kt1 = 0;
        }
        else {
            if (stateFilter.getValue().equals("Đã thanh toán")) {
                kt1 = 1;
            }
            else {
                kt1 = -1;
            }
        }
        if (requireFilter.getValue().equals("Tất cả")) {
            kt2 = 0;
        }
        else {
            if (requireFilter.getValue().equals("Bắt buộc")) {
                kt2 = 1;
            }
            else {
                kt2 = -1;
            }
        }
        if (dueFilter.getValue().equals("Tất cả")) {
            kt3 = 0;
        }
        else {

            if (dueFilter.getValue().equals("Đã quá hạn")) {
                kt3 = 1;
            }
            else {
                kt3 = -1;
            }
        }
        masterData = controller.getPaymentByResidentFilters(kt1, kt2, kt3, searchFilter.getText());
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
        HBox searchBar = new HBox(new TextFlow(new Text("Danh sách các khoản thu:")));
        searchBar.getStyleClass().add("big-text");
        mainContent.setPadding(new Insets(20, 50, 10, 50));
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

        filter.getChildren().add(new TextComboBox<String>("Trạng thái: ", FXCollections.observableArrayList("Tất cả", "Đã thanh toán", "Chưa thanh toán"), false, 150, "state-filter", false, "Tất cả"));
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextComboBox<String>("Loại: ", FXCollections.observableArrayList("Tất cả", "Bắt buộc", "Không bắt buộc"), false, 200, "require-filter", false, "Tất cả"));
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextComboBox<String>("Hạn: ", FXCollections.observableArrayList("Tất cả", "Đã quá hạn", "Chưa quá hạn"), false, 180, "due-filter", false, "Tất cả"));
        filter.getChildren().add(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextAndTextField("Theo từ khóa: ", "", "Enter the search keyword", "searchFilter", true));


        ((ComboBox<String>) scene.lookup("#state-filter")).setOnAction(event -> {
            reloadTable(scene);
        });

        ((ComboBox<String>) scene.lookup("#require-filter")).setOnAction(event -> {
            reloadTable(scene);
        });

        ((ComboBox<String>) scene.lookup("#due-filter")).setOnAction(event -> {
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

        var col0 = new TableColumn<PaymentProjectionDTO, String>("Hạn nộp(yyyy-MM-dd HH:mm)");

        col0.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDueDate() != null ? c.getValue().getDueDate().format(dateTimeFormatter) : ""
                )
        );

        var col1 = new TableColumn<PaymentProjectionDTO, String>("Phòng");

        col1.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getDueDate() != null ? c.getValue().getApartmentName() : ""
                )
        );

        var col2 = new TableColumn<PaymentProjectionDTO, String>("Loại");
        col2.setCellValueFactory(
                c -> {
                    if (c.getValue().getRequired().booleanValue()) {
                        return new SimpleStringProperty("Bắt buộc");
                    }
                    return new SimpleStringProperty("Không bắt buộc");
                }
        );

        var col3 = new TableColumn<PaymentProjectionDTO, String>("Nội dung khoản thu");
        col3.setCellValueFactory(
                c -> {
                    return new SimpleStringProperty(c.getValue().getContent());
                }
        );

        var col4 = new TableColumn<PaymentProjectionDTO, Double>("Số tiền(vnđ)");
        col4.setCellValueFactory(
                c -> {
                    return new SimpleObjectProperty<>(c.getValue().getAmount());
                }
        );

        var col5 = new TableColumn<PaymentProjectionDTO, String>("Trạng thái");
        col5.setCellValueFactory(
                c -> {
                    if (c.getValue().getPayTime() == null) {
                        return new SimpleStringProperty("Chưa thanh toán");
                    }
                    return new SimpleStringProperty("Đã thanh toán");
                }
        );

        var col6 = new TableColumn<PaymentProjectionDTO, HBox>("Thao tác");
        col6.setCellValueFactory(
                c -> {
                    FontIcon pencilIcon = new FontIcon();
                    pencilIcon.getStyleClass().add("pencil-icon");

                    FontIcon trashIcon = new FontIcon();
                    trashIcon.getStyleClass().add("trash-icon");

                    FontIcon payIcon = new FontIcon();
                    payIcon.getStyleClass().add("pay-icon");

                    FontIcon receipt = new FontIcon();
                    receipt.getStyleClass().add("receipt-icon");

                    Button btnEdit   = new Button("", pencilIcon);
                    Button btnDelete = new Button("", trashIcon);
                    Button btnPay    = new Button("", payIcon);
                    Button btnReceipt = new Button("", receipt);

                    btnEdit.getStyleClass().add("btn-edit");
                    btnDelete.getStyleClass().add("btn-delete");
                    btnPay.getStyleClass().add("btn-pay");
                    btnReceipt.getStyleClass().add("btn-receipt");

                    HBox hbox = new HBox(5, btnEdit);
                    if (c.getValue().getPayTime() == null) {
                        btnPay.setOnAction(event -> {
                            showPayPopup(JavaFxApplication.getCurrentStage(), c.getValue().getPaymentId());
                        });
                        hbox.getChildren().add(btnPay);
                        btnReceipt.setOnAction(event -> {
                            showReceiptPopup(JavaFxApplication.getCurrentStage(), c.getValue());
                        });
                        hbox.getChildren().add(btnReceipt);
                    }
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    btnEdit.setOnAction(event -> {
                        showInfoPopup(JavaFxApplication.getCurrentStage(), c.getValue().getBillId());
                    });

                    return new SimpleObjectProperty(hbox);
                }
        );

        if (table == null) {
            table = new TableView<PaymentProjectionDTO>();
            pagination = new Pagination();
            //        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            masterData = FXCollections.observableArrayList();
        }
        table.getColumns().setAll(col0, col1, col2, col3, col4, col5, col6);
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
        ObservableList<PaymentProjectionDTO> pageData = FXCollections.observableArrayList(
                masterData.subList(fromIndex, toIndex));

        table.setItems(pageData);
    }


    private void showInfoPopup(Stage ownerStage, Integer billId) {
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
        Scene popupScene = controller.getBillInfoScene(new Scene(overlay), billId);
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


    private void showPayPopup(Stage ownerStage, Integer paymentId) {

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
        Label popupTitle = new Label("Link thanh toán khoản thu");
        popupTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox popupButtons = new HBox(50);
        Button quit = new Button("OK");
        quit.getStyleClass().add("auto-addnew-button");
        popupButtons.setAlignment(Pos.CENTER);
        popupButtons.getChildren().addAll(quit);

        try {
            Image qrImage = new Image(new ByteArrayInputStream(controller.getBillPaymentLink(paymentId)));
//            Image qrImage = QRUtil.generateQRCode(controller.getBillPaymentLink(paymentId), 200, 200);
            ImageView qrView = new ImageView(qrImage);
            qrView.setFitHeight(200);
            qrView.setPreserveRatio(true);

            Label popupMessage = new Label("Đây là qr");
            popupMessage.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
            popupMessage.setWrapText(true);

            popupContent.getChildren().addAll(popupTitle, qrView, popupButtons);
        } catch (Exception e) {
            popupContent.getChildren().addAll(popupTitle);

        }

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
            reloadTable(scene);
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

    private void showReceiptPopup(Stage ownerStage, PaymentProjectionDTO payment) {

        // Tạo một stage mới cho popup
        Stage popupStage = new Stage();

        // Áp dụng hiệu ứng blur cho nội dung chính
        GaussianBlur blur = new GaussianBlur(10); // Độ mờ có thể điều chỉnh
        mainContent.setEffect(blur);

        // Panel chứa nội dung popup
        VBox popupContent = new VBox(20);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setMaxWidth(ScreenUtils.getScreenWidth() * 0.6);
        popupContent.setMaxHeight(ScreenUtils.getScreenHeight() * 0.8);
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0.0, 0.0);");

        // Nội dung của popup

        HBox popupButtons = new HBox(50);
        Button quit = new Button("OK");
        quit.getStyleClass().add("auto-addnew-button");
        popupButtons.setAlignment(Pos.CENTER);
        popupButtons.getChildren().addAll(quit);

        VBox infoBox = new VBox(5);
        infoBox.getChildren().addAll(
                new Label("Khách hàng: phòng " + payment.getApartmentName()),
                new Label("Khoản thu: " + payment.getContent())
        );
        infoBox.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<InvoiceItem> table = new TableView<>();

        TableColumn<InvoiceItem, String> nameCol = new TableColumn<>("Tên sản phẩm");
        nameCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getName()));

        TableColumn<InvoiceItem, Integer> qtyCol = new TableColumn<>("Số lượng");
        qtyCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getQuantity()));

        TableColumn<InvoiceItem, Double> priceCol = new TableColumn<>("Đơn giá");
        priceCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrice()));

        TableColumn<InvoiceItem, Double> totalCol = new TableColumn<>("Thành tiền");
        totalCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getTotal()));

        totalCol.setCellFactory(col -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormatter.format(item));
                }
            }
        });

        priceCol.setCellFactory(col -> new TableCell<InvoiceItem, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormatter.format(item));
                }
            }
        });

        table.getColumns().addAll(nameCol, qtyCol, priceCol, totalCol);
        Styles.toggleStyleClass(table, Styles.STRIPED);

        // === Dữ liệu mẫu ===
        ObservableList<InvoiceItem> data = FXCollections.observableArrayList(controller.getReceipt(payment.getPaymentId()));
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        double totalAmount = data.stream().mapToDouble(InvoiceItem::getTotal).sum();
        Label totalLabel = new Label("Tổng cộng: " + decimalFormatter.format(totalAmount) + " VNĐ");

        popupContent.getChildren().addAll(infoBox, table, totalLabel, popupButtons);

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
            reloadTable(scene);
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
