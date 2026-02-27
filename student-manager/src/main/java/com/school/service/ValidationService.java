package com.school.service;

import com.school.domain.Student;

public class ValidationService {

    public static void validate(Student s){

        if(s.getStudentId().isEmpty())
            throw new RuntimeException("Student ID required");

        if(s.getFullName().isEmpty())
            throw new RuntimeException("Name required");

        if(s.getGpa()<0 || s.getGpa()>4)
            throw new RuntimeException("Invalid GPA");
    }
}

