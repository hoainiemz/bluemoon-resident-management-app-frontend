package org.example.hellofx.controller;

import org.example.hellofx.model.*;
import org.example.hellofx.model.enums.AccountType;
import org.example.hellofx.service.FeedbackService;
import org.example.hellofx.service.NoticementService;
import org.example.hellofx.service.NotificationService;
import org.example.hellofx.ui.JavaFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HomeController{
    @Autowired
    private ProfileController profileController;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NoticementService noticementService;

    private static final int NUM_NOTIES = 20;
    @Autowired
    private FeedbackService feedbackService;

    public void logoutButtonClicked(){
        profileController.logOutRequest();
    }

    public void passwordChangeButtonClicked() {
        assert profileController.isLoggedIn();
        JavaFxApplication.showThemeScene(PasswordChangeScene.class);
    }

    public void danhSachDanCuClicked() {
        JavaFxApplication.showThemeScene(ResidentScene.class);
    }

    public void thongTinCaNhanClicked() {
        JavaFxApplication.showUserInformationScene();
    }

    public void taoKhoanThuClicked() {
        JavaFxApplication.showThemeScene(BillCreationScene.class);
    }

    public void hienThiCacYeuCauClicked() {
        JavaFxApplication.showThemeScene(AllResidentRequestScene.class);
    }

    public void danhSachKhoanThuClicked() {
        JavaFxApplication.showThemeScene(BillResidentScene.class);
    }

    public void quanLyKhoanThuClicked() {
        JavaFxApplication.showThemeScene(BillManagementScene.class);
    }

    public void taoThongBaoClicked() {
        JavaFxApplication.showThemeScene(NotificationCreationScene.class);
    }

    public void quanLyThongBaoClicked() {
        JavaFxApplication.showThemeScene(NotificationManagementScene.class);
    }

    public List<NotificationItem> getNotificationList(Integer residentId, boolean unReadOnly) {
        for (int i = 0; i < 10; i++) {
            try {
                return notificationService.findTopByResidentIdAndWatchedStatusOrderByCreatedAtDesc(residentId, unReadOnly);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public List<Feedback> getFeedbackList(boolean unreadOnly) {
        for (int i = 0; i < 10; i++) {
            try {
                return feedbackService.getTopFeedbackByWatchedStatusOrderByCreatedAtDesc(unreadOnly);
            }
            catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public Resident getResident() {
        return profileController.getResident();
    }

    public void noticementClicked(Noticement notice) {
        for (int i = 0; i < 10; i++) {
            try {
                noticementService.markAsWatched(notice.getNotificationId(), notice.getResidentId());
                break;
            }
            catch (Exception e) {
                continue;
            }
        }
    }

    public Account getProfile() {
        return profileController.getProfile();
    }

    public String getProfileNameRequest() {
        return profileController.getProfileNameRequest();
    }

    public void themCanHoClicked() {
        JavaFxApplication.showThemeScene(ApartmentCreationScene.class);
    }

    public void danhSachCanHoClicked() {
        JavaFxApplication.showThemeScene(ApartmentScene.class);
    }

    public void phuongTienClicked() {
        JavaFxApplication.showThemeScene(VehicleScene.class);
    }

    public void khieuNaiClicked() {
        JavaFxApplication.showThemeScene(FeedbackCreationScene.class);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackService.save(feedback);
    }

    public void dashBoardClicked() {
        JavaFxApplication.showThemeScene(DashboardScene.class);
    }

    public int getNumNoti() {
        if (profileController.getProfile().getRole() == AccountType.Resident) {
            return noticementService.countUnwatchedByResidentId(getResident().getResidentId());
        }
        return getFeedbackList(true).size();
    }
}
