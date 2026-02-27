package com.school.ui;

import com.school.repository.StudentRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.school.repository.SQLiteStudentRepository;

public class DashboardController {

    private final StudentRepository repo = new SQLiteStudentRepository();

    private Label totalLabel;
    private Label activeLabel;
    private Label inactiveLabel;
    private Label avgGpaLabel;

    public VBox view(Runnable goStudents,
                     Runnable goReports,
                     Runnable goImport,
                     Runnable goExport,
                     Runnable goSettings) {

        // Statistic labels
        totalLabel = createValueLabel();
        activeLabel = createValueLabel();
        inactiveLabel = createValueLabel();
        avgGpaLabel = createValueLabel();

        refresh();

        // Cards row 1
        HBox row1 = new HBox(20,
                createCard("Total Students", totalLabel),
                createCard("Active Students", activeLabel)
        );

        // Cards row 2
        HBox row2 = new HBox(20,
                createCard("Inactive Students", inactiveLabel),
                createCard("Average GPA", avgGpaLabel)
        );

        row1.setAlignment(Pos.CENTER_LEFT);
        row2.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(30, row1, row2);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("content-area");

        return root;
    }

    private Label createValueLabel() {
        Label label = new Label("0");
        label.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");
        return label;
    }

    private VBox createCard(String title, Label value) {

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        VBox card = new VBox(10, titleLabel, value);
        card.setPadding(new Insets(20));
        card.setMinWidth(250);
        card.getStyleClass().add("card");

        return card;
    }

    public void refresh() {
        try {
            var students = repo.findAll();

            long total = students.size();
            long active = students.stream()
                    .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                    .count();
            long inactive = total - active;

            double avgGpa = students.stream()
                    .mapToDouble(s -> s.getGpa())
                    .average()
                    .orElse(0.0);

            totalLabel.setText(String.valueOf(total));
            activeLabel.setText(String.valueOf(active));
            inactiveLabel.setText(String.valueOf(inactive));
            avgGpaLabel.setText(String.format("%.2f", avgGpa));

        } catch (Exception e) {
            totalLabel.setText("0");
            activeLabel.setText("0");
            inactiveLabel.setText("0");
            avgGpaLabel.setText("0.00");
        }
    }
}