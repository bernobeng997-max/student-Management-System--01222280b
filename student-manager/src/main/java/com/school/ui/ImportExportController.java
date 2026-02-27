package com.school.ui;

import com.school.domain.Student;
import com.school.service.StudentService;
import com.school.util.CSVExporter;
import com.school.util.CSVImporter;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ImportExportController {

    private final StudentService service = new StudentService();

    public VBox view(){

        Button importBtn = new Button("Import CSV");
        Button exportBtn = new Button("Export All Students");
        Button exportTopBtn = new Button("Export Top 10 Performers");
        Button exportRiskBtn = new Button("Export At Risk Students");

        importBtn.setOnAction(e -> importCSV());
        exportBtn.setOnAction(e -> exportCSV());
        exportTopBtn.setOnAction(e -> exportTop10());
        exportRiskBtn.setOnAction(e -> exportAtRisk());

        VBox box = new VBox(15,
                importBtn,
                exportBtn,
                exportTopBtn,
                exportRiskBtn
        );

        box.setPadding(new Insets(30));
        return box;
    }

    // ================= IMPORT =================

    private void importCSV(){

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files","*.csv")
        );

        File file = chooser.showOpenDialog(null);
        if(file == null) return;

        List<Student> students = CSVImporter.importFile(file.getAbsolutePath());

        int success = 0;
        int failed = 0;

        for(Student s : students){

            try{
                service.add(s);
                success++;
            }catch(Exception e){
                failed++;

                try{
                    java.nio.file.Files.writeString(
                            java.nio.file.Path.of("data/import_errors.CSV"),
                            "Duplicate ID: "+s.getStudentId()+"\n",
                            java.nio.file.StandardOpenOption.CREATE,
                            java.nio.file.StandardOpenOption.APPEND
                    );
                }catch(Exception ignored){}
            }
        }

        new Alert(Alert.AlertType.INFORMATION,
                "Import Summary" +
                        "\nSuccessful: " + success +
                        "\nErrors: " + failed +
                        "\nError report saved to: data/import_errors.csv"
        ).showAndWait();

    }

    // ================= EXPORT ALL =================

    private void exportCSV(){

        try{
            CSVExporter.exportAll(service.getAll());

            new Alert(Alert.AlertType.INFORMATION,
                    "Export completed!\nSaved to data/students.csv").showAndWait();

        }catch(Exception e){
            new Alert(Alert.AlertType.ERROR,"Export failed").showAndWait();
        }
    }

    // ================= EXPORT TOP 10 =================

    private void exportTop10(){

        try{

            List<Student> top =
                    service.getAll().stream()
                            .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                            .limit(10)
                            .collect(Collectors.toList());

            CSVExporter.exportTop10(top);

            new Alert(Alert.AlertType.INFORMATION,
                    "Top 10 exported!\nSaved to data/top10.csv").showAndWait();

        }catch(Exception e){
            new Alert(Alert.AlertType.ERROR,"Top 10 export failed").showAndWait();
        }
    }

    // ================= EXPORT AT RISK =================

    private void exportAtRisk(){

        try{

            // Default threshold = 2.0
            List<Student> risk = service.atRisk(2.0);

            if(risk.isEmpty()){
                new Alert(Alert.AlertType.WARNING,
                        "No at-risk students found").showAndWait();
                return;
            }

            CSVExporter.exportAtRisk(risk);

            new Alert(Alert.AlertType.INFORMATION,
                    "At-risk students exported!\nSaved to data/Atrisk.csv").showAndWait();

        }catch(Exception e){
            new Alert(Alert.AlertType.ERROR,"At risk export failed").showAndWait();
        }
    }
}
