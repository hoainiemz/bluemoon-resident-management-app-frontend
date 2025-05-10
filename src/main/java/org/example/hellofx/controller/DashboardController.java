package org.example.hellofx.controller;

import org.example.hellofx.model.Account;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DashboardController {
    @Autowired
    private ProfileController profileController;

    public Account getProfile() {
        return profileController.getProfile();
    }

    public void danhSachCanHoClicked() {
        JavaFxApplication.showThemeScene(ApartmentScene.class);
    }

    public void danhSachDanCuClicked() {
        JavaFxApplication.showThemeScene(ResidentScene.class);
    }

    public void phuongTienClicked() {
        JavaFxApplication.showThemeScene(VehicleScene.class);
    }

    public void quanLyKhoanThuClicked() {
        JavaFxApplication.showThemeScene(BillManagementScene.class);
    }

    public void quanLyThongBaoClicked() {
        JavaFxApplication.showThemeScene(NotificationManagementScene.class);
    }
}
