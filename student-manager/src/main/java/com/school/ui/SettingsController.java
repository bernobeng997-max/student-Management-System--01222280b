package com.school.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {

    private final File file = new File("settings.txt");

    public VBox view(){

        // ================= EXISTING =================
        TextField username = new TextField();
        PasswordField password = new PasswordField();

        // ================= NEW GPA THRESHOLD =================
        TextField gpaThreshold = new TextField();

        // ================= NEW PROGRAMME MANAGEMENT =================
        TextField programmeField = new TextField();
        ListView<String> programmeList = new ListView<>();

        load(username, password, gpaThreshold, programmeList);

        Button addProgramme = new Button("Add Programme");
        Button removeProgramme = new Button("Remove Selected");
        Button save = new Button("Save Settings");

        addProgramme.setOnAction(e -> {
            if(!programmeField.getText().isBlank()){
                programmeList.getItems().add(programmeField.getText());
                programmeField.clear();
            }
        });

        removeProgramme.setOnAction(e ->
                programmeList.getItems().remove(
                        programmeList.getSelectionModel().getSelectedItem()
                )
        );

        save.setOnAction(e -> {
            try(FileWriter fw = new FileWriter(file)){

                // Existing
                fw.write(username.getText() + "\n");
                fw.write(password.getText() + "\n");

                // New GPA threshold
                fw.write(gpaThreshold.getText() + "\n");

                // Programmes
                for(String prog : programmeList.getItems()){
                    fw.write(prog + "\n");
                }

                alert("Settings saved successfully");

            } catch(Exception ex){
                alert(ex.getMessage());
            }
        });

        // Layout
        GridPane adminGrid = new GridPane();
        adminGrid.setHgap(10);
        adminGrid.setVgap(10);



        VBox layout = new VBox(15,
                adminGrid,
                new Label("Manage Programmes"),
                programmeField,
                addProgramme,
                programmeList,
                removeProgramme,
                save
        );

        layout.setPadding(new Insets(20));

        return layout;
    }

    private void load(TextField u,
                      PasswordField p,
                      TextField gpa,
                      ListView<String> programmeList){

        try{
            if(file.exists()){

                List<String> lines = Files.readAllLines(file.toPath());

                if(lines.size() >= 3){
                    u.setText(lines.get(0));
                    p.setText(lines.get(1));
                    gpa.setText(lines.get(2));
                }

                // Load programmes (after first 3 lines)
                if(lines.size() > 3){
                    programmeList.getItems()
                            .addAll(lines.subList(3, lines.size()));
                }
            }
        }catch(Exception ignored){}
    }

    private void alert(String m){
        new Alert(Alert.AlertType.INFORMATION,m).showAndWait();
    }
}
