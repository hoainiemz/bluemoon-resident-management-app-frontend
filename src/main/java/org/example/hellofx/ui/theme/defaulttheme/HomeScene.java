package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.example.hellofx.controller.HomeController;
import org.example.hellofx.model.Feedback;
import org.example.hellofx.model.Noticement;
import org.example.hellofx.model.NotificationItem;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.utils.ScreenUtils;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class HomeScene implements ThemeScene {
    @Autowired
    HomeController controller;

    private Scene scene;
    private List menuButtonList;
    private ScrollPane notiScrollPane;
    private VBox notiContainer, notiList;
    private RadioButton allNoti, unReadNoti;

    private void resetMenuBar(Scene scene) {
        VBox menuContainer = (VBox) scene.lookup("#menuContainer");
        for (int i = 0; i < menuContainer.getChildren().size(); i++) {
            VBox cur = (VBox) menuContainer.getChildren().get(i);
            RadioButton radioButton = (RadioButton) menuButtonList.get(i);
            for (int j = 1; j < cur.getChildren().size(); j++) {
                Button tmp = (Button) cur.getChildren().get(j);
                tmp.setVisible(radioButton.isSelected());
                tmp.setManaged(radioButton.isSelected());
            }
            Button tmp = (Button) cur.getChildren().get(0);
            tmp.getStyleClass().remove("selected-main-menu");
            if (radioButton.isSelected()) {
                tmp.getStyleClass().add("selected-main-menu");
            }
        }
    }

    public Scene getScene(Scene scene) {
//        final Scene scene = JavaFxApplication.getCurrentScene();
        menuButtonList = new ArrayList<RadioButton>();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/themes/default-theme/home/home.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException exception) {
            return null;
        }
        this.scene = scene;
        scene.getStylesheets().add("/themes/default-theme/home/home.css");

        HBox topBar = (HBox) scene.lookup("#topBar");
        HBox container = (HBox) scene.lookup("#container");
        topBar.setPrefHeight(ScreenUtils.getScreenHeight() * 0.1);
        container.setPrefHeight(ScreenUtils.getScreenHeight() * 0.9);

        // top bar
        topBar.requestFocus();
        HBox topMenu = (HBox) scene.lookup("#topMenu");
        HBox topProfile = (HBox) scene.lookup("#topProfile");
        topMenu.setPrefWidth(ScreenUtils.getScreenWidth() * 0.6);
        topProfile.setPrefWidth(ScreenUtils.getScreenWidth() * 0.4);
        topProfile.setSpacing(0);
        topBar.setOnMouseClicked(event -> {
            topBar.requestFocus(); // Move focus to the VBox
        });

        //top menu
        HBox logoContainer = (HBox) scene.lookup("#logoContainer");
        topMenu.setAlignment(Pos.CENTER_LEFT);
        Text logoLabel = new Text("Blue Moon Resident Management Application");
        Image image = new Image("images/blue-moon-logo.png");
        ImageView logo = new ImageView(image);
        logo.setPreserveRatio(true);
        logo.setFitHeight(topBar.getPrefHeight() * 0.8);
        logoContainer.getChildren().addAll(logo, logoLabel);
        topMenu.setSpacing(20);
        logoContainer.setSpacing(10);
        logoContainer.setPadding(new Insets(0, 40, 0, 40));
        logoContainer.setAlignment(Pos.CENTER_LEFT);

        Button notificationButton = (Button) scene.lookup("#notificationButton");
        FontIcon notiIcon = new FontIcon(MaterialDesign.MDI_BELL_RING_OUTLINE);
        notificationButton.setGraphic(notiIcon);
        notificationButton.setPrefHeight(topBar.getPrefHeight());
        notificationButton.setPrefWidth(topBar.getPrefHeight());
        notiIcon.getStyleClass().add("notification-button-icon");
        // top profile
        HBox profileContainer = (HBox) scene.lookup("#profileContainer");
        topProfile.setAlignment(Pos.CENTER_RIGHT);
        profileContainer.setAlignment(Pos.CENTER_RIGHT);
        Text welcome = null;
        if (controller.getResident().getFirstName() != null) {
            welcome = new Text("Chào mừng bạn, " + controller.getResident().getFirstName());
        }
        else {
            welcome = new Text("Chào mừng bạn, " + controller.getProfileNameRequest());
        }
        Image userIcon = new Image("images/user-icon.jpg");
        welcome.setId("welcome");
        ImageView userImg = new ImageView(userIcon);
        userImg.setFitHeight(topBar.getPrefHeight() * 0.5);
        userImg.setFitWidth(userImg.getFitHeight());
        profileContainer.getChildren().addAll(userImg, welcome);
        profileContainer.setSpacing(20);
        profileContainer.setPadding(new Insets(0, 40, 0, 40));
        VBox profileDropDownContent = new VBox();
        profileDropDownContent.getStyleClass().add("profile-drop-down");
        profileDropDownContent.setAlignment(Pos.CENTER);
        profileDropDownContent.setPadding(new Insets(10, 0, 10, 0));
        profileDropDownContent.getChildren().addAll(
                new TextFlow(new Text(controller.getProfileNameRequest())),
                new TextFlow(new Text("Đổi mật khẩu")),
                new TextFlow(new Text("Đăng xuất")),
                new TextFlow(new Text("Đóng ứng dụng"))
        );

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setSpread(0.3);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        Popup popup = new Popup();
        Popup notiPopup = createNotiPopup();

        popup.getContent().add(profileDropDownContent);
        popup.setWidth(profileContainer.getPrefWidth());
        profileDropDownContent.setPrefWidth(profileContainer.getPrefWidth());
        profileContainer.setOnMouseClicked(event -> {
            if (popup.isShowing()) {
                popup.hide();
            }
            else {
                if (notiPopup.isShowing()) {
                    notiPopup.hide();
                }
                double x = profileContainer.localToScreen(profileContainer.getBoundsInLocal()).getMinX();
                double y = profileContainer.localToScreen(profileContainer.getBoundsInLocal()).getMaxY() - 5;
                profileDropDownContent.setPrefWidth(profileContainer.getWidth() - 5);
                popup.show(this.scene.getWindow(), x, y);
            }
        });
        profileDropDownContent.getChildren().get(2).setOnMouseClicked(event -> {
            popup.hide();
            controller.logoutButtonClicked();
        });
        profileDropDownContent.getChildren().get(1).setOnMouseClicked(event -> {
            popup.hide();
            controller.passwordChangeButtonClicked();
        });
        notificationButton.setOnAction(event -> {
            if (notiPopup.isShowing()) {
                notiPopup.hide();
            }
            else {
                if (popup.isShowing()) {
                    popup.hide();
                }
                double x = notificationButton.localToScreen(notificationButton.getBoundsInLocal()).getMinX();
                double y = notificationButton.localToScreen(notificationButton.getBoundsInLocal()).getMaxY();
                notiScrollPane.setPrefWidth((notificationButton.getWidth() + profileContainer.getWidth()) * 1.2 - 10);
                notiScrollPane.setPrefHeight(container.getHeight() * 0.8);
                notiScrollPane.setEffect(dropShadow);
                showNotificationList();
                notiPopup.show(this.scene.getWindow(), x, y);
            }
        });
        profileDropDownContent.getChildren().get(3).setOnMouseClicked(event -> {
            Platform.exit();
        });
        profileDropDownContent.setPrefWidth(ScreenUtils.getScreenWidth() * 0.2);

        // container
        VBox menuContainer = (VBox) scene.lookup("#menuContainer");
        StackPane content = (StackPane) scene.lookup("#content");
        content.setPrefHeight(container.getPrefHeight());
        menuContainer.setPrefWidth(ScreenUtils.getScreenWidth() * 0.1);
        content.setPrefWidth(ScreenUtils.getScreenWidth() * 0.9);
        content.setPrefHeight(container.getPrefHeight());
        content.setOnMouseClicked(event -> {
            content.requestFocus(); // Move focus to the VBox
        });
        menuContainer.setOnMouseClicked(event -> {
            menuContainer.requestFocus(); // Move focus to the VBox
        });
        // left menu bar

        // dashBoard
        VBox dashBoardContainer = new VBox();
        Button dashBoard = new Button("Dashboard");
        dashBoard.getStyleClass().add("menu-main-button");
        dashBoardContainer.getChildren().addAll(dashBoard);
        dashBoard.setOnAction(event -> {
           controller.dashBoardClicked();
        });
        // dan cu
        VBox danCuContainer = new VBox();
        Button danCu = new Button("Dân cư");
        danCu.getStyleClass().add("menu-main-button");
        Button danhSachDanCu = new Button("Danh sách dân cư");
        danhSachDanCu.getStyleClass().add("menu-sub-button");
        Button hienThiCacYeuCau = new Button("Các yêu cầu tham gia");
        hienThiCacYeuCau.getStyleClass().add("menu-sub-button");
        danCuContainer.getChildren().addAll(danCu, danhSachDanCu);
        if (controller.getProfile().getRole() != AccountType.Resident) {
            danCuContainer.getChildren().addAll(hienThiCacYeuCau);
        }
        danhSachDanCu.setOnAction(event -> {
            controller.danhSachDanCuClicked();
        });
        hienThiCacYeuCau.setOnAction(event -> {
            controller.hienThiCacYeuCauClicked();
        });
        // Can ho
        VBox canHoContainer = new VBox();
        Button canHo = new Button("Căn hộ");
        canHo.getStyleClass().add("menu-main-button");
        Button danhSachCanHo = new Button("Danh sách căn hộ");
        Button themCanHo = new Button("Thêm căn hộ");
        danhSachCanHo.getStyleClass().add("menu-sub-button");
        themCanHo.getStyleClass().add("menu-sub-button");
        canHoContainer.getChildren().addAll(canHo, danhSachCanHo);
        danhSachCanHo.setOnAction(event -> {
            controller.danhSachCanHoClicked();
        });
        themCanHo.setOnAction(event -> {
            controller.themCanHoClicked();
        });
        // khoan thu
        VBox khoanThuContainer = new VBox();
        Button khoanThu = new Button("Khoản thu");
        khoanThu.getStyleClass().add("menu-main-button");
        Button danhSachKhoanThu = new Button("Các khoản thu");
        danhSachKhoanThu.getStyleClass().add("menu-sub-button");
        Button taoKhoanThu = new Button("Tạo khoản thu");
        taoKhoanThu.getStyleClass().add("menu-sub-button");
        Button quanLyKhoanThu = new Button("Quản lý khoản thu");
        quanLyKhoanThu.getStyleClass().add("menu-sub-button");
        khoanThuContainer.getChildren().addAll(khoanThu);
        if (controller.getProfile().getRole() != AccountType.Resident) {
            khoanThuContainer.getChildren().addAll(quanLyKhoanThu);
        }
        else {
            khoanThuContainer.getChildren().addAll(danhSachKhoanThu);
        }
        danhSachKhoanThu.setOnAction(event -> {
           controller.danhSachKhoanThuClicked();
        });
        taoKhoanThu.setOnAction(event -> {
            controller.taoKhoanThuClicked();
        });
        quanLyKhoanThu.setOnAction(event -> {
            controller.quanLyKhoanThuClicked();
        });

        // thong bao
        VBox thongBaoContainer = new VBox();
        Button thongBao = new Button("Thông báo");
        thongBao.getStyleClass().add("menu-main-button");
        Button taoThongBao = new Button("Tạo thông báo");
        taoThongBao.getStyleClass().add("menu-sub-button");
        Button quanLyThongBao = new Button("Quản lý thông báo");
        quanLyThongBao.getStyleClass().add("menu-sub-button");
        taoThongBao.setOnAction(event -> {
            controller.taoThongBaoClicked();
        });

        quanLyThongBao.setOnAction(event -> {
            controller.quanLyThongBaoClicked();
        });

        thongBaoContainer.getChildren().addAll(thongBao, quanLyThongBao);


        // Tien ich
        VBox tienIchContainer = new VBox();
        Button tienIch = new Button("Tiện ích");
        tienIch.getStyleClass().add("menu-main-button");
        Button phuongTien = new Button("Phương tiện");
        phuongTien.getStyleClass().add("menu-sub-button");
        Button khieuNai = new Button("Gửi khiếu nại");
        khieuNai.getStyleClass().add("menu-sub-button");
        tienIchContainer.getChildren().addAll(tienIch, phuongTien);
        phuongTien.setOnAction(event -> {
            controller.phuongTienClicked();
        });
        if (controller.getProfile().getRole() == AccountType.Resident) {
            tienIchContainer.getChildren().addAll(khieuNai);
        }
        khieuNai.setOnAction(event -> {
            controller.khieuNaiClicked();
        });

        // my profile
        VBox myProfileContainer = new VBox();
        Button myProfile = new Button("My Profile");
        myProfile.getStyleClass().add("menu-main-button");
        Button thongTinCaNhan = new Button("Thông tin cá nhân");
        thongTinCaNhan.setOnAction(event -> {
            controller.thongTinCaNhanClicked();
        });
        thongTinCaNhan.getStyleClass().add("menu-sub-button");
        Button doiMatKhau = new Button("Đổi mật khẩu");
        doiMatKhau.getStyleClass().add("menu-sub-button");
        doiMatKhau.setOnAction(event -> {
            controller.passwordChangeButtonClicked();
        });
        myProfileContainer.getChildren().addAll(myProfile, thongTinCaNhan, doiMatKhau);

        ToggleGroup menuToggleGroup = new ToggleGroup();

        menuContainer.getChildren().addAll(dashBoardContainer, danCuContainer, canHoContainer, khoanThuContainer);
        if (controller.getProfile().getRole() != AccountType.Resident) {
            menuContainer.getChildren().add(thongBaoContainer);
        }
        menuContainer.getChildren().addAll(tienIchContainer, myProfileContainer);
        for (int i = 0; i < menuContainer.getChildren().size(); i++) {
            RadioButton tmp = new RadioButton();
            tmp.setToggleGroup(menuToggleGroup);
            menuButtonList.add(tmp);
            VBox curr = (VBox) menuContainer.getChildren().get(i);
            Button cur = (Button) curr.getChildrenUnmodifiable().get(0);
            cur.setOnMouseClicked(event -> {
                Node tgt = (Node) event.getSource();
                tgt = tgt.getParent();
                int idx = menuContainer.getChildren().indexOf(tgt);
                RadioButton radio = (RadioButton) menuButtonList.get(idx);
                radio.setSelected(true);
                resetMenuBar(this.scene);
            });
            curr.setPrefWidth(menuContainer.getPrefWidth());
            for (int j = 0; j < curr.getChildren().size(); j++) {
                Button curButton = (Button) curr.getChildren().get(j);
                curButton.setPrefWidth(curr.getPrefWidth());
            }
        }
        RadioButton tmp = (RadioButton) menuButtonList.get(0);
        tmp.setSelected(true);
        resetMenuBar(scene);
        return scene;
    }

    public Popup createNotiPopup() {
        Popup popup = new Popup();
        notiScrollPane = new ScrollPane();
        notiScrollPane.setId("noti-popup-scroll-pane");
        popup.getContent().add(notiScrollPane);
        notiContainer = new VBox();
        notiScrollPane.setContent(notiContainer);
        return popup;
    }

    public void showNotificationList() {
        notiContainer.getChildren().clear();
        Text thongBaoText = new Text("Thông báo:");
        if (controller.getProfile().getRole() != AccountType.Resident) {
            thongBaoText.setText("Khiếu nại:");
        }
        thongBaoText.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 30px;");
        notiContainer.getChildren().add(thongBaoText);
//        notiContainer.setStyle("-fx-background-color: red");
        notiContainer.setPrefWidth(notiScrollPane.getPrefWidth());
        notiContainer.setSpacing(15);
        notiContainer.setPadding(new Insets(10, 15, 10, 15));
        HBox statusContainer = new HBox();
        allNoti = new RadioButton("Tất cả");
        unReadNoti = new RadioButton("Chưa đọc");
        var statusGroup = new ToggleGroup();
        allNoti.setToggleGroup(statusGroup);
        unReadNoti.setToggleGroup(statusGroup);
        allNoti.getStyleClass().add("custom-radio-button");
        unReadNoti.getStyleClass().add("custom-radio-button");
        allNoti.setSelected(true);
        statusContainer.getChildren().addAll(allNoti, unReadNoti);
        statusContainer.setSpacing(15);
        notiContainer.getChildren().addAll(statusContainer);
        notiList = new VBox();
        notiList.setSpacing(8);
        notiContainer.getChildren().addAll(notiList);
        updateNotificationList();
        allNoti.setOnAction(event -> {
            updateNotificationList();
        });
        unReadNoti.setOnAction(event -> {
            updateNotificationList();
        });
    }

    private void updateNotificationList() {
        notiList.getChildren().clear();
        if (controller.getProfile().getRole() == AccountType.Resident) {
            List<NotificationItem> ls = controller.getNotificationList(controller.getResident().getResidentId(), unReadNoti.isSelected());
            ls.forEach(item -> {
                var mess = new Message(item.getTitle(), item.getMessage(), null);
                mess.setOnMouseClicked(event -> {
                    Noticement noticement = new Noticement(null, item.getId(), controller.getResident().getResidentId(), true);
                    controller.noticementClicked(noticement);
                    updateNotificationList();
                });
                mess.getStyleClass().add("hand-hover");
                switch (item.getType()) {
                    case "Info":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_INFORMATION_OUTLINE));
                        mess.getStyleClass().add(Styles.ACCENT);
                        break;
                    case "Success":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_CHECK_CIRCLE_OUTLINE));
                        mess.getStyleClass().add(Styles.SUCCESS);
                        break;
                    case "Warning":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_ALERT_OUTLINE));
                        mess.getStyleClass().add(Styles.WARNING);
                        break;
                    case "Danger":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_ALERT_CIRCLE_OUTLINE));
                        mess.getStyleClass().add(Styles.DANGER);
                        break;
                    default:
                        assert false;
                }
                notiList.getChildren().add(mess);
            });
        }
        else {
            List<Feedback> ls = controller.getFeedbackList(unReadNoti.isSelected());
            ls.forEach(item -> {
                var mess = new Message(item.getTitle(), item.getContent(), null);
                mess.setOnMouseClicked(event -> {
                    Feedback feedback = new Feedback(item.getResidentId(), item.getTitle(), item.getContent());
                    feedback.setFeedbackId(item.getFeedbackId());
                    feedback.setWatched(true);

                    controller.saveFeedback(feedback);
                    updateNotificationList();
                });
                mess.getStyleClass().add("hand-hover");
                Tooltip tooltip = new Tooltip(item.getResidentId().toString());
                tooltip.setShowDelay(Duration.seconds(0.1));
//                Tooltip.install(mess, tooltip);
                mess.setTooltip(tooltip);
                switch (item.getType()) {
                    case "Info":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_INFORMATION_OUTLINE));
                        mess.getStyleClass().add(Styles.ACCENT);
                        break;
                    case "Success":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_CHECK_CIRCLE_OUTLINE));
                        mess.getStyleClass().add(Styles.SUCCESS);
                        break;
                    case "Warning":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_ALERT_OUTLINE));
                        mess.getStyleClass().add(Styles.WARNING);
                        break;
                    case "Danger":
                        mess.setGraphic(new FontIcon(MaterialDesign.MDI_ALERT_CIRCLE_OUTLINE));
                        mess.getStyleClass().add(Styles.DANGER);
                        break;
                    default:
                        assert false;
                }
                notiList.getChildren().add(mess);
            });
        }
    }
}
