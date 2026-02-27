package com.school.service;

import com.school.domain.Student;
import com.school.repository.SQLiteStudentRepository;
import com.school.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

public class StudentService {

    private final StudentRepository repo = new SQLiteStudentRepository();

    // ================= ADD STUDENT =================

    public void add(Student s) {

        validateAdvanced(s);          // NEW
        ValidationService.validate(s);

        if (repo.exists(s.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }

        s.setDateAdded(LocalDate.now().toString());
        s.setStatus("ACTIVE");

        repo.add(s);
    }


    // ================= DELETE =================

    public void delete(String id){
        repo.delete(id);
    }

    // ================= FETCH =================

    public List<Student> getAll(){
        return repo.getAll();
    }

    // ================= DASHBOARD METRICS =================

    public int totalStudents(){
        return repo.getAll().size();
    }

    public long activeStudents(){
        return repo.getAll()
                .stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .count();
    }

    public long inactiveStudents(){
        return repo.getAll()
                .stream()
                .filter(s -> "INACTIVE".equalsIgnoreCase(s.getStatus()))
                .count();
    }

    public double averageGpa(){
        return repo.getAll()
                .stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0);
    }
    public void update(Student s){
        ValidationService.validate(s);
        repo.update(s);
    }
    public List<Student> atRisk(double threshold){

        return repo.getAll()
                .stream()
                .filter(s -> s.getGpa() < threshold)
                .toList();
    }
    private void validateAdvanced(Student s){

        // Student ID
        if(!s.getStudentId().matches("[A-Za-z0-9]{4,20}"))
            throw new RuntimeException("Student ID must be 4–20 letters/digits");

        // Full name
        if(!s.getFullName().matches("[A-Za-z ]{2,60}"))
            throw new RuntimeException("Full name must be 2–60 letters only");

        // Programme
        if(s.getProgramme()==null || s.getProgramme().isBlank())
            throw new RuntimeException("Programme required");

        // Level
        if(!List.of(100,200,300,400,500,600,700).contains(s.getLevel()))
            throw new RuntimeException("Invalid level");

        // GPA
        if(s.getGpa() < 0 || s.getGpa() > 4)
            throw new RuntimeException("GPA must be between 0.0 and 4.0");

        // Email
        if(!s.getEmail().matches(".+@.+\\..+"))
            throw new RuntimeException("Invalid email format");

        // Phone
        if(!s.getPhone().matches("\\d{10,15}"))
            throw new RuntimeException("Phone must be 10–15 digits");
    }

}

