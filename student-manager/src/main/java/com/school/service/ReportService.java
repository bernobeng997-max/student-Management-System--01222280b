package com.school.service;

import com.school.domain.Student;
import com.school.repository.SQLiteStudentRepository;
import com.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    private final StudentRepository repo = new SQLiteStudentRepository();

    // Top 10 performers
    public List<Student> topPerformers(String programme, Integer level){

        return repo.getAll().stream()
                .filter(s -> programme == null || s.getProgramme().equals(programme))
                .filter(s -> level == null || s.getLevel() == level)
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // At risk students
    public List<Student> atRisk(double threshold){

        return repo.getAll().stream()
                .filter(s -> s.getGpa() < threshold)
                .collect(Collectors.toList());
    }

    // GPA distribution
    public Map<String,Long> gpaDistribution(){

        return repo.getAll().stream()
                .collect(Collectors.groupingBy(
                        s -> {
                            double g = s.getGpa();
                            if(g < 2) return "<2.0";
                            if(g < 3) return "2.0–2.99";
                            if(g < 3.5) return "3.0–3.49";
                            return "3.5–4.0";
                        },
                        Collectors.counting()
                ));
    }

    // Programme summary
    public Map<String, Long> programmeSummary(){

        return repo.getAll().stream()
                .collect(Collectors.groupingBy(
                        Student::getProgramme,
                        Collectors.counting()
                ));
    }
}




