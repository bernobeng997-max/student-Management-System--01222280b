package com.school.ui;

import com.school.domain.Student;
import com.school.service.StudentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class StudentsController {

    private final StudentService service = new StudentService();
    private final ObservableList<Student> data = FXCollections.observableArrayList();
    private final TableView<Student> table = new TableView<>();

    private final TextField searchField = new TextField();
    private final ComboBox<String> levelFilter = new ComboBox<>();
    private final ComboBox<String> programmeFilter = new ComboBox<>();
    private final ComboBox<String> statusFilter = new ComboBox<>();

    public BorderPane view(){

        searchField.setPromptText("Search ID or Name");

        levelFilter.getItems().addAll("All","100","200","300","400","500","600","700");
        levelFilter.setValue("All");

        programmeFilter.getItems().addAll("All","Computer Engineering","Electrical Engineering","IT");
        programmeFilter.setValue("All");

        statusFilter.getItems().addAll("All","Active","Inactive");
        statusFilter.setValue("All");

        // ================= TABLE ===================

        TableColumn<Student,String> id = new TableColumn<>("ID");
        id.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStudentId()));

        TableColumn<Student,String> name = new TableColumn<>("Name");
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFullName()));

        TableColumn<Student,String> prog = new TableColumn<>("Programme");
        prog.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProgramme()));

        TableColumn<Student,String> level = new TableColumn<>("Level");
        level.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getLevel()));

        TableColumn<Student,String> gpa = new TableColumn<>("GPA");
        gpa.setCellValueFactory(c -> new SimpleStringProperty(""+c.getValue().getGpa()));

        gpa.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                }else{
                    setText(item);
                    setTextFill(Double.parseDouble(item) < 2 ? Color.RED : Color.BLACK);
                }
            }
        });

        TableColumn<Student,String> Phone = new TableColumn<>("Phone");
        Phone.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));


        TableColumn<Student,String> Email = new TableColumn<>("Email");
        Email.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));


        TableColumn<Student,String> date = new TableColumn<>("Date Added");
        date.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getDateAdded()));


        TableColumn<Student,String> status = new TableColumn<>("Status");
        status.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        table.getColumns().addAll(id,name,prog,level,gpa,status,date,Email,Phone);

        refresh();

        // ================= FILTER PIPELINE ===================

        FilteredList<Student> filtered = new FilteredList<>(data, p -> true);

        Runnable applyFilters = () -> {

            filtered.setPredicate(s -> {

                String search = searchField.getText().toLowerCase();
                String lvl = levelFilter.getValue();
                String progF = programmeFilter.getValue();
                String stat = statusFilter.getValue();

                boolean matchSearch =
                        s.getStudentId().toLowerCase().contains(search)
                                || s.getFullName().toLowerCase().contains(search);

                boolean matchLevel =
                        lvl.equals("All Levels") || (""+s.getLevel()).equals(lvl);

                boolean matchProgramme =
                        progF.equals("All Programmes") || s.getProgramme().equalsIgnoreCase(progF);

                boolean matchStatus =
                        stat.equals("All Statuses") || s.getStatus().equalsIgnoreCase(stat);

                return matchSearch && matchLevel && matchProgramme && matchStatus;
            });
        };

        searchField.textProperty().addListener((a,b,c)->applyFilters.run());
        levelFilter.valueProperty().addListener((a,b,c)->applyFilters.run());
        programmeFilter.valueProperty().addListener((a,b,c)->applyFilters.run());
        statusFilter.valueProperty().addListener((a,b,c)->applyFilters.run());

        SortedList<Student> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        // ================= FORM ===================

        TextField tfId = new TextField();
        TextField tfName = new TextField();
        TextField tfProg = new TextField();
        TextField tfLevel = new TextField();
        TextField tfGpa = new TextField();
        TextField tfEmail = new TextField();
        TextField tfPhone = new TextField();

        table.setOnMouseClicked(e -> {
            if(e.getClickCount()==2){
                Student s = table.getSelectionModel().getSelectedItem();
                if(s!=null){
                    tfId.setText(s.getStudentId());
                    tfName.setText(s.getFullName());
                    tfProg.setText(s.getProgramme());
                    tfLevel.setText(""+s.getLevel());
                    tfGpa.setText(""+s.getGpa());
                    tfEmail.setText(s.getEmail());
                    tfPhone.setText(s.getPhone());
                }
            }
        });

        Button add = new Button("Add");
        Button edit = new Button("Edit");
        Button del = new Button("Delete");
        Button sortGpa = new Button("Sort by GPA");
        Button sortName = new Button("Sort by Name");

        sortGpa.setOnAction(e ->
                table.getSortOrder().setAll(gpa));

        sortName.setOnAction(e ->
                table.getSortOrder().setAll(name));


        add.setOnAction(e -> {

            if(tfId.getText().isBlank() || tfName.getText().isBlank()){
                alert("Student ID and Full Name required");
                return;
            }

            try{

                service.add(new Student(
                        tfId.getText(),
                        tfName.getText(),
                        tfProg.getText(),
                        Integer.parseInt(tfLevel.getText()),
                        Double.parseDouble(tfGpa.getText()),
                        tfEmail.getText(),
                        tfPhone.getText(),
                        "",
                        "Active"
                ));


                refresh();
                if (UIFactory.dashboardRefresher != null) {
                    UIFactory.dashboardRefresher.run();
                }

                clear(tfId,tfName,tfProg,tfLevel,tfGpa,tfEmail,tfPhone);

            }catch(Exception ex){
                alert(ex.getMessage());
            }
        });


        edit.setOnAction(e -> {

            Student sel = table.getSelectionModel().getSelectedItem();
            if(sel==null){ alert("Select student"); return; }

            try{

                service.update(new Student(
                        tfId.getText(),
                        tfName.getText(),
                        tfProg.getText(),
                        Integer.parseInt(tfLevel.getText()),
                        Double.parseDouble(tfGpa.getText()),
                        tfEmail.getText(),
                        tfPhone.getText(),
                        sel.getDateAdded(),
                        sel.getStatus()
                ));

                refresh();
                if (UIFactory.dashboardRefresher != null) {
                    UIFactory.dashboardRefresher.run();
                }

                clear(tfId,tfName,tfProg,tfLevel,tfGpa,tfEmail,tfPhone);

            }catch(Exception ex){
                alert(ex.getMessage());
            }
        });

        del.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if(s==null){ alert("Select student"); return; }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete " + s.getFullName() + "?",
                    ButtonType.YES, ButtonType.NO);

            confirm.showAndWait();

            if(confirm.getResult() == ButtonType.YES) {
                service.delete(s.getStudentId());
                refresh();
                if (UIFactory.dashboardRefresher != null) {
                    UIFactory.dashboardRefresher.run();
                }
            }
            });


        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setVgap(5);

        form.addRow(0,new Label("ID"),tfId);
        form.addRow(1,new Label("Name"),tfName);
        form.addRow(2,new Label("Programme"),tfProg);
        form.addRow(3,new Label("Level"),tfLevel);
        form.addRow(4,new Label("GPA"),tfGpa);
        form.addRow(5,new Label("Email"),tfEmail);
        form.addRow(6,new Label("Phone"),tfPhone);
        form.add(add,1,7);
        form.add(edit,1,8);
        form.add(del,1,9);

        ToolBar filters = new ToolBar(
                searchField,
                levelFilter,
                programmeFilter,
                statusFilter,
                sortGpa,
                sortName
        );


        BorderPane root = new BorderPane();
        root.setTop(filters);
        root.setCenter(table);
        root.setRight(form);

        return root;
    }

    private void refresh(){
        data.setAll(service.getAll());
    }

    private void clear(TextField... f){
        for(TextField t:f) t.clear();
    }

    private void alert(String m){
        new Alert(Alert.AlertType.ERROR,m).showAndWait();
    }
}
