package org.example.hellofx.ui.theme.defaulttheme;

import atlantafx.base.controls.Calendar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.example.hellofx.controller.DashboardController;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.MyClock;
import org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes.MyItem;
import org.example.hellofx.utils.ScreenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.scene.control.Label;

import java.time.LocalDate;

@Component
public class DashboardScene extends Notificable implements ThemeScene{
    @Autowired
    private DashboardController controller;

    private Scene scene;
    private VBox mainContent;
    private ScrollPane scrollPane;

    public Scene getCurrentScene() {
        return scene;
    }

    public Scene getScene(Scene scene) {
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

        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setId("main-content");
        mainContent.setPadding(new Insets(20, 50, 10, 50));
        TextFlow section = new TextFlow(new Text("Main Dashboard:"));
        section.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section);

        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.CENTER);

        topHBox.setPrefHeight(ScreenUtils.getScreenHeight() * 0.45);
        topHBox.setPrefWidth(mainContent.getPrefWidth() * 0.9);
        mainContent.getChildren().addAll(topHBox);
        mainContent.setSpacing(30);

        VBox UL = new VBox(40);
        var UR = new Calendar(LocalDate.now());
        UR.setShowWeekNumbers(true);
        UR.setTopNode(new MyClock());
//        UR.setMaxHeight(topHBox.getPrefHeight());
        UR.getStylesheets().add(
                getClass().getResource("/themes/default-theme/home/calendar-style.css").toExternalForm()
        );


        topHBox.getChildren().addAll(UL, UR);
        topHBox.setSpacing(ScreenUtils.getScreenHeight() * 0.05);
        UL.setPadding(new Insets(60, 40, 0, 40));

        UL.setPrefWidth(topHBox.getPrefWidth() * 0.6);
        UR.setPrefWidth(topHBox.getPrefWidth() * 0.3);
        UR.setMinWidth(topHBox.getPrefWidth() * 0.3);
        UR.setMaxWidth(topHBox.getPrefWidth() * 0.3);
        UR.setPrefHeight(topHBox.getPrefHeight());
        UR.setMinHeight(topHBox.getPrefHeight());
        UR.setMaxHeight(topHBox.getPrefHeight());
        UL.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #6A00FF 0%, #8C1AFF 50%, #9D00FF 100%);");
        UR.setStyle("-fx-background-color: #fdfdfe;");
        UL.getStyleClass().add("public-dashboard");
        UR.getStyleClass().add("public-dashboard");

        Label welcome = new Label("Welcome, " + controller.getProfile().getRole() + "!");
        welcome.setId("welcomeText");

        Label quote = new Label("Smart tools. Smooth living.");
        quote.getStyleClass().add("subText");
        UL.getChildren().addAll(welcome, quote);

        if (controller.getProfile().getRole() == AccountType.Resident) {
            return scene;
        }

        TextFlow section2 = new TextFlow(new Text("Giao diện quản trị:"));
        section2.getStyleClass().add("big-text");
        mainContent.getChildren().addAll(section2);

        HBox bottomHBox = new HBox();
        bottomHBox.setSpacing(20);

        MyItem danCu = new MyItem("Dân cư");
        danCu.getButton().setOnAction(event -> {
            controller.danhSachDanCuClicked();
        });

        MyItem phong = new MyItem("Căn hộ");
        phong.getButton().setOnAction(event -> {
            controller.danhSachCanHoClicked();
        });

        MyItem phuongTien = new MyItem("Phương tiện");
        phuongTien.getButton().setOnAction(event -> {
            controller.phuongTienClicked();
        });

        MyItem khoanThu = new MyItem("Khoản thu");
        khoanThu.getButton().setOnAction(event -> {
            controller.quanLyKhoanThuClicked();
        });

        MyItem thongBao = new MyItem("Thông báo");
        thongBao.getButton().setOnAction(event -> {
            controller.quanLyThongBaoClicked();
        });

        bottomHBox.getChildren().addAll(danCu, phong, phuongTien, khoanThu, thongBao);

        mainContent.getChildren().add(bottomHBox);

        return scene;
    }
}
