package org.example.hellofx.scheduler;

import javafx.application.Platform;
import org.example.hellofx.SpringBootFxApplication;
import org.example.hellofx.ui.theme.defaulttheme.HomeScene;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotiCountScheduler {
    @Scheduled(fixedRate = 3000)
    public void updateNotiCount() {
        try {
            if (Platform.isFxApplicationThread() || isJavaFxInitialized()) {
                Platform.runLater(() -> {
                    HomeScene theme = SpringBootFxApplication.context.getBean(HomeScene.class);
                    theme.resetNumNoti();
                });
            }
        } catch (IllegalStateException e) {
            // JavaFX not initialized yet, skip this update cycle
            System.out.println("JavaFX not initialized yet, skipping notification update");
        }
    }

    private boolean isJavaFxInitialized() {
        try {
            // Try to check if we can access Platform - this will throw if not initialized
            Platform.runLater(() -> {});
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

}