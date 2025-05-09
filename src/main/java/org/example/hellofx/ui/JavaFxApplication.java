package org.example.hellofx.ui;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.controller.ProfileController;
import org.example.hellofx.model.Account;
import org.example.hellofx.model.Resident;
import org.example.hellofx.ui.theme.ThemeScene;
import org.example.hellofx.ui.theme.defaulttheme.BillInformationScene;
import org.example.hellofx.ui.theme.defaulttheme.LoginScene;
import org.example.hellofx.ui.theme.defaulttheme.NotificationInformationScene;
import org.example.hellofx.ui.theme.defaulttheme.UserInformationScene;
import org.example.hellofx.utils.ScreenUtils;

public class JavaFxApplication extends Application {
    private static Stage currentStage;

    public static Stage getCurrentStage() {
        return currentStage;
    }

    public static Scene getCurrentScene() {
        return currentStage.getScene();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        currentStage = stage;
//        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        currentStage.setTitle("Hello World");
        LoginScene theme = SpringBootFxApplication.context.getBean(LoginScene.class);
        showThemeScene(LoginScene.class);
        currentStage.setWidth(ScreenUtils.getScreenWidth());
        currentStage.setHeight(ScreenUtils.getScreenHeight());

        currentStage.initStyle(StageStyle.UNDECORATED); // Remove the upper bar
        currentStage.setFullScreen(true);
        currentStage.setFullScreenExitHint(""); // Disable the ESC message
        currentStage.setFullScreenExitKeyCombination(null); // Disable ESC key for exit

        currentStage.show();
        currentStage.show();
    }

    public static <T> void showThemeScene(Class<T> clazz){
        ThemeScene theme = (ThemeScene) SpringBootFxApplication.context.getBean(clazz);
        currentStage.setScene(theme.getScene(getCurrentScene()));
    }

    public static void showUserInformationScene() {
        UserInformationScene theme = SpringBootFxApplication.context.getBean(UserInformationScene.class);
        ProfileController profileController = SpringBootFxApplication.context.getBean(ProfileController.class);
        currentStage.setScene(theme.getScene(profileController.getProfile(), profileController.getResident(), getCurrentScene()));
    }

    public static void showUserInformationScene(Account profile, Resident resident) {
        UserInformationScene theme = SpringBootFxApplication.context.getBean(UserInformationScene.class);
        currentStage.setScene(theme.getScene(profile, resident, getCurrentScene()));
    }

    public static void showBillInformationScene(Integer billId) {
        BillInformationScene theme = SpringBootFxApplication.context.getBean(BillInformationScene.class);
        currentStage.setScene(theme.getScene(billId, getCurrentScene()));
    }

    public static void showNotificationInformationScene(Integer notiId) {
        NotificationInformationScene theme = SpringBootFxApplication.context.getBean(NotificationInformationScene.class);
        currentStage.setScene(theme.getScene(notiId, getCurrentScene()));
    }
}
