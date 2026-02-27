package com.school.service;

import com.school.domain.Student;
import com.school.repository.SQLiteStudentRepository;
import com.school.util.DB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    private ReportService service;
    private SQLiteStudentRepository repo;

    private Student createStudent(String id, String programme, int level, double gpa) {
        return new Student(
                id,
                "Samuel Obeng " + id,
                programme,
                level,
                gpa,
                "sam@gmail.com",
                "0240000000",
                "2026-02-26",
                "ACTIVE"
        );
    }

    @BeforeEach
    void setUp() throws Exception {

        service = new ReportService();
        repo = new SQLiteStudentRepository();

        // Clean database
        Statement st = DB.get().createStatement();
        st.executeUpdate("DELETE FROM students");
        st.close();
    }

    @Test
    void topPerformersShouldReturnHighestGpaFirst() {

        repo.add(createStudent("013333333b", "Computer Science", 200, 2.0));
        repo.add(createStudent("014444444b", "Electrical Engineering", 400, 3.9));
        repo.add(createStudent("015555555b", "IT", 200, 3.5));

        List<Student> result = service.topPerformers("Electrical Engineering", 400);

        assertEquals("S2", result.get(0).getStudentId());
    }

    @Test
    void atRiskShouldReturnStudentsBelowThreshold() {

        repo.add(createStudent("01666666b", "Computer Science", 200, 1.5));
        repo.add(createStudent("01777777b", "Computer Science", 200, 3.0));

        List<Student> result = service.atRisk(2.0);

        assertEquals(1, result.size());
        assertEquals("01777777b", result.get(0).getStudentId());
    }

    @Test
    void gpaDistributionShouldGroupCorrectly() {

        repo.add(createStudent("S6", "CS", 200, 1.8)); // <2
        repo.add(createStudent("S7", "CS", 200, 2.5)); // 2–2.99
        repo.add(createStudent("S8", "CS", 200, 3.2)); // 3–3.49
        repo.add(createStudent("S9", "CS", 200, 3.8)); // 3.5–4

        Map<String, Long> dist = service.gpaDistribution();

        assertEquals(1, dist.get("<2.0"));
        assertEquals(1, dist.get("2.0–2.99"));
        assertEquals(1, dist.get("3.0–3.49"));
        assertEquals(1, dist.get("3.5–4.0"));
    }

    @Test
    void programmeSummaryShouldCountStudentsPerProgramme() {

        repo.add(createStudent("018888888b", "Computer science", 200, 3.0));
        repo.add(createStudent("019999999b", "Computer Science", 200, 3.2));
        repo.add(createStudent("S10000000b", "IT", 200, 3.5));

        Map<String, Long> summary = service.programmeSummary();

        assertEquals(2, summary.get("Computer Science"));
        assertEquals(1, summary.get("IT"));
    }
}