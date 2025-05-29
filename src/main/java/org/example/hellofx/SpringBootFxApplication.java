package org.example.hellofx;

import javafx.application.Application;
import org.example.hellofx.ui.JavaFxApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.im.InputContext;

@EnableScheduling
@SpringBootApplication
public class SpringBootFxApplication {
    public static ApplicationContext context;

    public static void main(String[] args) {
        InputContext.getInstance();
        context = SpringApplication.run(SpringBootFxApplication.class, args);
        Application.launch(JavaFxApplication.class, args);
    }
}