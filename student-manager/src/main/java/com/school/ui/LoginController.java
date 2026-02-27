package com.school.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginController {

    // SIMPLE OFFLINE CREDENTIALS
    private static final String USER = "admin";
    private static final String PASS = "1234";

    public void show(Stage stage){

        TextField username = new TextField();
        PasswordField password = new PasswordField();

        Button login = new Button("Login");

        Label msg = new Label();

        login.setOnAction(e -> {

            if(USER.equals(username.getText())
                    && PASS.equals(password.getText())){

                // SUCCESS → LOAD MAIN UI
                UIFactory.loadMain(stage);

            } else {
                msg.setText("Invalid login");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0,new Label("Username:"),username);
        grid.addRow(1,new Label("Password:"),password);
        grid.addRow(2,login);
        grid.addRow(3,msg);

        stage.setScene(new Scene(grid,350,220));
        stage.setTitle("Login");
        stage.show();
    }
}

