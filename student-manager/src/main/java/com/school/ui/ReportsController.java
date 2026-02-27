package com.school.ui;

import com.school.domain.Student;
import com.school.service.ReportService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ReportsController {

    private final ReportService service = new ReportService();
    private final TableView<Student> table = new TableView<>();
    private final ObservableList<Student> data = FXCollections.observableArrayList();

    public BorderPane view() {

        setupTable();

        TabPane tabs = new TabPane();

        tabs.getTabs().addAll(
                topPerformersTab(),
                atRiskTab(),
                distributionTab()
        );

        Button exportBtn = new Button("Export Report");
        exportBtn.setOnAction(e -> exportCSV());

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setTop(tabs);
        root.setCenter(table);
        root.setBottom(exportBtn);

        return root;
    }

    // ================= TABLE SETUP =================

    private void setupTable() {

        TableColumn<Student,String> id = new TableColumn<>("ID");
        id.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStudentId()));

        TableColumn<Student,String> name = new TableColumn<>("Name");
        name.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));

        TableColumn<Student,String> prog = new TableColumn<>("Programme");
        prog.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProgramme()));

        TableColumn<Student,String> level = new TableColumn<>("Level");
        level.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(""+c.getValue().getLevel()));

        TableColumn<Student,String> gpa = new TableColumn<>("GPA");
        gpa.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(""+c.getValue().getGpa()));

        table.getColumns().addAll(id,name,prog,level,gpa);
        table.setItems(data);
    }

    // ================= TOP PERFORMERS =================

    private Tab topPerformersTab() {

        ComboBox<String> programmeBox = new ComboBox<>();
        programmeBox.getItems().add("All");
        programmeBox.setValue("All");

        TextField levelField = new TextField();
        levelField.setPromptText("Level (optional)");

        Button load = new Button("Generate");

        load.setOnAction(e -> {

            String programme = programmeBox.getValue().equals("All") ? null : programmeBox.getValue();
            Integer level = levelField.getText().isBlank() ? null : Integer.parseInt(levelField.getText());

            List<Student> list = service.topPerformers(programme, level);
            data.setAll(list);
        });

        VBox box = new VBox(10, new Label("Programme"), programmeBox,
                new Label("Level"), levelField, load);

        Tab tab = new Tab("Top Performers");
        tab.setContent(box);
        tab.setClosable(false);

        return tab;
    }

    // ================= AT RISK =================

    private Tab atRiskTab() {

        TextField threshold = new TextField("2.0");
        Button load = new Button("Generate");

        load.setOnAction(e -> {
            double limit = Double.parseDouble(threshold.getText());
            data.setAll(service.atRisk(limit));
        });

        VBox box = new VBox(10,
                new Label("GPA Threshold"),
                threshold,
                load
        );

        Tab tab = new Tab("At Risk");
        tab.setContent(box);
        tab.setClosable(false);

        return tab;
    }

    // ================= GPA DISTRIBUTION =================

    private Tab distributionTab() {

        Button load = new Button("Generate");

        load.setOnAction(e -> {
            data.clear();

            Map<String,Long> dist = service.gpaDistribution();

            dist.forEach((range,count)->{
                Student dummy = new Student(
                        "",
                        range,
                        "",
                        0,
                        count,
                        "",
                        "",
                        "",
                        ""
                );
                data.add(dummy);
            });
        });

        VBox box = new VBox(10, load);

        Tab tab = new Tab("GPA Distribution");
        tab.setContent(box);
        tab.setClosable(false);

        return tab;
    }

    // ================= EXPORT =================

    private void exportCSV() {

        try(PrintWriter pw = new PrintWriter(new FileWriter("report.csv"))){

            pw.println("ID,Name,Programme,Level,GPA");

            for(Student s : data){
                pw.println(
                        s.getStudentId() + "," +
                                s.getFullName() + "," +
                                s.getProgramme() + "," +
                                s.getLevel() + "," +
                                s.getGpa()
                );
            }

            new Alert(Alert.AlertType.INFORMATION,"Report exported to report.csv").showAndWait();

        }catch(Exception ex){
            new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();
        }
    }
}
