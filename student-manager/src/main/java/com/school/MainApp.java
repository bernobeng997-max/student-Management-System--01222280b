package com.school;

import com.school.ui.UIFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // Directly load the main application interface
        UIFactory.loadMain(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}