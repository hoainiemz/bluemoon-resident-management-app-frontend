package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import org.example.hellofx.controller.VehicleController;
import org.example.hellofx.dto.VehicleInfo;
import org.example.hellofx.model.Validation;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.model.enums.ValidationState;
import org.example.hellofx.model.enums.VehicleType;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextAndTextField;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.TextComboBox;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndComboBox;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.VerticleTextAndTextField;
import org.example.hellofx.utils.ScreenUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class VehicleScene extends Notificable implements ThemeScene {
    @Autowired
    VehicleController controller;

    private Scene scene;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final int ITEMS_PER_PAGE = 9;
    private ObservableList<VehicleInfo> masterData;
    private TableView<VehicleInfo> table;
    private Pagination pagination;
    private VBox mainContent;
    private ScrollPane scrollPane;
    private Notification myInfo;
    private Stage popupStage;

    public void reset() {
        masterData = null;
        table = null;
        pagination = null;
        mainContent = null;
    }

    @Override
    protected Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Scene scene) {
        reset();
        this.scene = scene;
        HBox container = (HBox) scene.lookup("#container");
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
        TextFlow section = new TextFlow(new Text("Danh sách phương tiện:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);
        mainContent.setPadding(new Insets(20, 20, 10, 20));
        mainContent.setSpacing(20);

        if (controller.getProfile().getRole() != AccountType.Resident) {
            HBox addnewContrainer = new HBox();
            Button addnew = new Button("Thêm phương tiện mới");
            addnewContrainer.getChildren().add(addnew);
            addnew.getStyleClass().add("addnew-button");
            mainContent.getChildren().add(addnewContrainer);
            addnewContrainer.setAlignment(Pos.CENTER_LEFT);
            addnew.setOnAction(event -> {
                showAddingPopup(JavaFxApplication.getCurrentStage());
            });
        }

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

        filter.getChildren().add(new TextComboBox<String>("Theo phòng: ", controller.getAllApartmentName(), true, 100, "houseIdFilter"));
        filter.getChildren().addAll(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextComboBox<String>("Theo loại: ", FXCollections.observableArrayList("Tất cả", VehicleType.Car.toString(), VehicleType.Motorbike.toString()), false, 150, "typeFilter"));
        filter.getChildren().addAll(new Separator(Orientation.VERTICAL));
        filter.getChildren().add(new TextAndTextField("Theo biển số: ", "", "Enter the search keyword", "searchFilter", true));
        ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#typeFilter")).setValue("Tất cả");
        ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#houseIdFilter")).setOnAction(event -> {
            reloadTable(scene);
        });
        ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#typeFilter")).setOnAction(event -> {
            reloadTable(scene);
        });
        ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField().setOnAction(event -> {
            reloadTable(scene);
        });

        createTable();
        reloadTable(scene);

        mainContent.getChildren().addAll(table, pagination);

        return scene;
    }

    private void createTable () {
        var col1 = new TableColumn<VehicleInfo, String>("Ngày đăng ký");
        col1.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getRegistrationDate().format(formatter))
        );

        var col2 = new TableColumn<VehicleInfo, VehicleType>("Loại phương tiện");
        col2.setCellValueFactory(
                c -> new SimpleObjectProperty(c.getValue().getVehicleType())
        );

        var col3 = new TableColumn<VehicleInfo, String>("Biển số");
        col3.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getLicensePlate())
        );

        var col4 = new TableColumn<VehicleInfo, String>("Phòng");
        col4.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getApartmentName())
        );

        var col5 = new TableColumn<VehicleInfo, HBox>("Thao tác");
        col5.setCellValueFactory(
                c -> {
                    FontIcon trashIcon = new FontIcon(MaterialDesignT.TRASH_CAN);
                    trashIcon.setIconSize(16);
                    trashIcon.setStyle("-fx-icon-color: " + "#fa4547" + ";");

                    Button btnDelete = new Button("", trashIcon);

                    btnDelete.getStyleClass().add("btn-delete");
                    btnDelete.setCursor(Cursor.HAND);

                    HBox hbox = new HBox(5);

                    if (controller.getProfile().getRole() != AccountType.Resident) {
                        hbox.getChildren().add(btnDelete);
                        btnDelete.setOnAction(e -> {
                            showFullscreenPopup(JavaFxApplication.getCurrentStage(), c.getValue().getVehicleId());
                        });
                    }

                    return new SimpleObjectProperty(hbox);
                }
        );

        if (table == null) {
            table = new TableView<VehicleInfo>();
            pagination = new Pagination();
            masterData = FXCollections.observableArrayList();
        }
        table.getColumns().setAll(col1, col2, col3, col4);
        if (controller.getProfile().getRole() != AccountType.Resident) {
            table.getColumns().add(col5);
        }
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        table.setId("resident-table");
        Styles.toggleStyleClass(table, Styles.STRIPED);
    }

    private void reloadTable(Scene scene) {
        ComboBox<String> houseIdFilter = ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#houseIdFilter"));
        ComboBox<String> typeFilter = ((ComboBox<String>) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#typeFilter"));
        TextField searchFilter = ((TextAndTextField) ((ScrollPane) scene.lookup("ScrollPane")).getContent().lookup("#searchFilter")).getTextField();
        String kt1 = houseIdFilter.getValue(), kt3 = searchFilter.getText();
        VehicleType kt2;
        switch (typeFilter.getValue()) {
            case "Car":
                kt2 = VehicleType.Car;
                break;
            case "Motorbike":
                kt2 = VehicleType.Motorbike;
                break;
            default:
                kt2 = null;
                break;
        }
        masterData = controller.getVehicleInfoByFilter(kt1, kt2, kt3);
        resetPagination();
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
        ObservableList<VehicleInfo> pageData = FXCollections.observableArrayList(
                masterData.subList(fromIndex, toIndex));

        table.setItems(pageData);
    }

    private void showAddingPopup(Stage ownerStage) {
        // Tạo một stage mới cho popup
        popupStage = new Stage();

        // Áp dụng hiệu ứng blur cho nội dung chính
        GaussianBlur blur = new GaussianBlur(10); // Độ mờ có thể điều chỉnh
        scrollPane.getContent().setEffect(blur);

        // Panel chứa nội dung popup
        VBox popupContent = new VBox(20);
        popupContent.setPrefWidth(ScreenUtils.getScreenWidth() * 0.6);
        popupContent.setMaxWidth(ScreenUtils.getScreenWidth() * 0.6);
        popupContent.setAlignment(Pos.TOP_CENTER);
        popupContent.setMinHeight(Region.USE_COMPUTED_SIZE);
        popupContent.setPrefHeight(Region.USE_COMPUTED_SIZE);
        popupContent.setMaxHeight(Region.USE_COMPUTED_SIZE);
        popupContent.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0.0, 0.0);");

        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER_LEFT);
        TextFlow section = new TextFlow(new Text("Đăng ký phương tiện:"));
        titleContainer.getChildren().addAll(section);
        section.getStyleClass().add("big-text");

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.TOP_LEFT);
        ComboBox<String> houseNames = new ComboBox<>(controller.getAllApartmentName());
        ComboBox<VehicleType> vehicleTypes = new ComboBox<>(FXCollections.observableArrayList(VehicleType.values()));
        vehicleTypes.getSelectionModel().select(0);
        formContainer.getChildren().add(new VerticleTextAndComboBox("Phòng", houseNames, null, "houseIdInfo", true));
        formContainer.getChildren().add(new VerticleTextAndComboBox("Loại phương tiện", vehicleTypes, null, "vehicleType", true));
        formContainer.getChildren().add(new VerticleTextAndTextField("Biển số:", "", "Enter the license plate", "licensePlateInfo", true));
        houseNames.setEditable(true);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setSpacing(10);
        Button save = new Button("Lưu");
        Button close = new Button("Đóng");
        save.getStyleClass().add("login-button");
        close.getStyleClass().add("register-button");
        buttonContainer.getChildren().addAll(save, close);
        buttonContainer.setPadding(new Insets(10, 0, 10, 0));

        popupContent.getChildren().addAll(titleContainer, formContainer, buttonContainer);

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
        close.setOnAction(e -> {
            popupStage.close();
            scrollPane.getContent().setEffect(null); // Xóa hiệu ứng blur khi đóng popup
        });

        // Xử lý khi đóng popup bằng cách khác (X, Alt+F4)
        popupStage.setOnCloseRequest(e -> {
            scrollPane.getContent().setEffect(null);
        });

        save.setOnAction(e -> {
            String appartment = houseNames.getValue();
            VehicleType type = vehicleTypes.getValue();
            String searchedPlate = ((VerticleTextAndTextField) popupContent.lookup("#licensePlateInfo")).getTextField().getText();
            Validation vl = controller.roomCheck(appartment, houseNames.getItems().stream().toList());
            if (vl.state() == ValidationState.ERROR) {
                showMyPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            vl = controller.licensePlateCheck(searchedPlate);
            if (vl.state() == ValidationState.ERROR) {
                showMyPopUpMessage(vl.state().toString(), vl.message());
                return;
            }
            controller.save(appartment, type, searchedPlate);
            controller.reset();
            close.fire();
            showPopUpMessage("Thành công", "Đã đăng ký thành công phương tiện mới!");
        });
        overlay.requestFocus();
        // Hiển thị popup
        popupStage.show();
    }

    private void showMyPopUpMessage(String state, String message) {
        AnchorPane tmp = (AnchorPane) popupStage.getScene().lookup("AnchorPane");
        AnchorPane rightFrame = tmp;
        if (myInfo == null) {
            myInfo = new Notification(message);
            myInfo.getStyleClass().add(Styles.ELEVATED_1);
            myInfo.setMaxHeight(100);
        }
        else {
            myInfo.setMessage(message);
            try {
                rightFrame.getChildren().remove(myInfo);
            }
            catch (NullPointerException e) {
            }
        }
        try {
            myInfo.getStyleClass().remove(Styles.WARNING);
        }
        catch (NullPointerException e) {}
        try {
            myInfo.getStyleClass().remove(Styles.SUCCESS);
        }
        catch (NullPointerException e) {}
        if (state.toUpperCase().equals(ValidationState.ERROR.toString())) {
            myInfo.getStyleClass().add(Styles.WARNING);
            myInfo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE));
        }
        else {
            myInfo.getStyleClass().add(Styles.SUCCESS);
            myInfo.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE));
        }
        myInfo.setOnClose(event -> {
            rightFrame.getChildren().remove(myInfo);
        });
        rightFrame.getChildren().add(myInfo);
        AnchorPane.setBottomAnchor(myInfo, 10.0);
        AnchorPane.setRightAnchor  (myInfo, 10.0);
    }

    private void showFullscreenPopup(Stage ownerStage, Integer vehicleId) {
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
        Label popupTitle = new Label("Xóa phương tiện");
        popupTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label popupMessage = new Label("Thao tác này sẽ phương tiện được chọn.\nBạn có chắc muốn tiếp tục.");
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
            controller.deleteVehicleByVehicleId(vehicleId);
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
