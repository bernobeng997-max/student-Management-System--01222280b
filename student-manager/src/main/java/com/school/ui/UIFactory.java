package com.school.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIFactory {

    public static Runnable dashboardRefresher;

    private static BorderPane root;
    private static BorderPane contentArea;

    public static void loadMain(Stage stage){

        root = new BorderPane();
        contentArea = new BorderPane();

        VBox sidebar = createSidebar();

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(
                UIFactory.class.getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();

        showDashboard();
    }

    private static VBox createSidebar(){

        Button dashboardBtn = new Button("Dashboard");
        Button studentsBtn = new Button("Students");
        Button reportsBtn = new Button("Reports");
        Button importBtn = new Button("Import / Export");
        Button settingsBtn = new Button("Settings");

        dashboardBtn.getStyleClass().add("sidebar-button");
        studentsBtn.getStyleClass().add("sidebar-button");
        reportsBtn.getStyleClass().add("sidebar-button");
        importBtn.getStyleClass().add("sidebar-button");
        settingsBtn.getStyleClass().add("sidebar-button");

        dashboardBtn.setOnAction(e -> showDashboard());
        studentsBtn.setOnAction(e -> showStudents());
        reportsBtn.setOnAction(e -> showReports());
        importBtn.setOnAction(e -> showImport());
        settingsBtn.setOnAction(e -> showSettings());

        VBox box = new VBox(15,
                dashboardBtn,
                studentsBtn,
                reportsBtn,
                importBtn,
                settingsBtn
        );

        box.setPadding(new Insets(20));
        box.getStyleClass().add("sidebar");

        return box;
    }

    private static void showDashboard(){

        DashboardController controller = new DashboardController();

        contentArea.setCenter(controller.view(
                UIFactory::showStudents,
                UIFactory::showReports,
                UIFactory::showImport,
                UIFactory::showImport,
                UIFactory::showSettings
        ));

        dashboardRefresher = controller::refresh;
    }

    private static void showStudents(){
        contentArea.setCenter(new StudentsController().view());
    }

    private static void showReports(){
        contentArea.setCenter(new ReportsController().view());
    }

    private static void showImport(){
        contentArea.setCenter(new ImportExportController().view());
    }

    private static void showSettings(){
        contentArea.setCenter(new SettingsController().view());
    }
}