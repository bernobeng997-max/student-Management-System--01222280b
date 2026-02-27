package com.school.util;

import com.school.domain.Student;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVExporter {

    public static void exportAll(List<Student> students){

        try {

            File dir = new File("data");
            if(!dir.exists()) dir.mkdir();

            FileWriter fw = new FileWriter("data/students.csv");

            fw.write("ID,Name,Programme,Level,GPA,Email,Phone,DateAdded,Status\n");

            for(Student s : students){
                fw.write(
                        s.getStudentId()+","+
                                s.getFullName()+","+
                                s.getProgramme()+","+
                                s.getLevel()+","+
                                s.getGpa()+","+
                                s.getEmail()+","+
                                s.getPhone()+","+
                                s.getDateAdded()+","+
                                s.getStatus()+"\n"
                );
            }

            fw.close();

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void exportTopPerformers(String path, List<Student> students) {

        try (FileWriter fw = new FileWriter(path)) {

            fw.write("Rank,ID,Name,Programme,Level,GPA\n");

            int rank = 1;

            for (Student s : students) {
                fw.write(
                        rank++ + "," +
                                s.getStudentId() + "," +
                                s.getFullName() + "," +
                                s.getProgramme() + "," +
                                s.getLevel() + "," +
                                s.getGpa() + "\n"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportTop10(List<Student> students){

        File dir = new File("data");
        if(!dir.exists()) dir.mkdir();

        try(FileWriter fw = new FileWriter("data/top10.csv")){

            fw.write("Rank,ID,Name,Programme,Level,GPA\n");

            int i = 1;
            for(Student s : students){
                fw.write(i++ + "," +
                        s.getStudentId() + "," +
                        s.getFullName() + "," +
                        s.getProgramme() + "," +
                        s.getLevel() + "," +
                        s.getGpa() + "\n");
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void exportAtRisk(List<Student> students){

        File dir = new File("data");
        if(!dir.exists()) dir.mkdir();

        try(FileWriter fw = new FileWriter("data/Atrisk.csv")){

            fw.write("ID,Name,Programme,Level,GPA,Email,Phone,Date Added,Status\n");

            for(Student s : students){
                fw.write(
                        s.getStudentId() + "," +
                                s.getFullName() + "," +
                                s.getProgramme() + "," +
                                s.getLevel() + "," +
                                s.getGpa() + "," +
                                s.getEmail() + "," +
                                s.getPhone() + "," +
                                s.getDateAdded() + "," +
                                s.getStatus() + "\n"
                );
            }

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
