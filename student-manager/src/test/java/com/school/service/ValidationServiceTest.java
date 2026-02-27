package com.school.service;

import com.school.domain.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationServiceTest {

    private Student createStudent(String id, String name, double gpa) {
        return new Student(
                id,
                name,
                "Computer Science",
                200,
                gpa,
                "sam@gmail.com",
                "0240000000",
                "2026-02-27",
                "ACTIVE"
        );
    }

    @Test
    void validStudentShouldPassValidation() {

        Student s = createStudent("013333333b", "Samuel Obeng", 3.5);

        assertDoesNotThrow(() -> ValidationService.validate(s));
    }

    @Test
    void emptyStudentIdShouldThrowException() {

        Student s = createStudent("", "Samuel Obeng", 3.5);

        assertThrows(RuntimeException.class,
                () -> ValidationService.validate(s));
    }

    @Test
    void emptyNameShouldThrowException() {

        Student s = createStudent("013333333b", "", 3.5);

        assertThrows(RuntimeException.class,
                () -> ValidationService.validate(s));
    }

    @Test
    void invalidLowGpaShouldThrowException() {

        Student s = createStudent("013333333b", "Samuel Obeng", -1.0);

        assertThrows(RuntimeException.class,
                () -> ValidationService.validate(s));
    }

    @Test
    void invalidHighGpaShouldThrowException() {

        Student s = createStudent("013333333b", "Samuel Obeng", 5.0);

        assertThrows(RuntimeException.class,
                () -> ValidationService.validate(s));
    }
}