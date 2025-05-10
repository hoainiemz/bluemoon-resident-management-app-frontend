package org.example.hellofx.ui.theme.defaulttheme.myhandmadenodes;

import atlantafx.base.theme.Styles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MyClock extends VBox {

    static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy");
    static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public MyClock() {
        var clockLbl = new Label(TIME_FORMATTER.format(
                LocalTime.now(ZoneId.systemDefault()))
        );
        clockLbl.getStyleClass().add(Styles.TITLE_2);

        var dateLbl = new Label(DATE_FORMATTER.format(
                LocalDate.now(ZoneId.systemDefault()))
        );

        // -fx-border-width: 0 0 0.5 0;
        // -fx-border-color: -color-border-default;
//        setStyle(
//                "-fx-border-width: 0 0 0.5 0;" +
//                "-fx-border-color: -color-border-default;"
//        );
        setSpacing(10);
        getChildren().setAll(clockLbl, dateLbl);

        var t = new Timeline(new KeyFrame(
                Duration.seconds(1),
                e -> {
                    var time = LocalTime.now(ZoneId.systemDefault());
                    clockLbl.setText(TIME_FORMATTER.format(time));
                }
        ));
        t.setCycleCount(Animation.INDEFINITE);
        t.playFromStart();
    }
}
