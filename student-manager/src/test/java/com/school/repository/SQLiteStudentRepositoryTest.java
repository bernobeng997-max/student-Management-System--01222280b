package com.school.repository;

import com.school.domain.Student;
import com.school.util.DB;
import org.junit.jupiter.api.*;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLiteStudentRepositoryTest {

    private SQLiteStudentRepository repo;

    private Student createStudent(String id, double gpa) {
        return new Student(
                id,
                "Test Samuel Obeng",
                "Computer Science",
                200,
                gpa,
                "sam@gmail.com",
                "0240000000",
                "2026-02-27",
                "ACTIVE"
        );
    }

    @BeforeEach
    void setUp() throws Exception {
        repo = new SQLiteStudentRepository();

        // Clean database before each test
        Statement st = DB.get().createStatement();
        st.executeUpdate("DELETE FROM students");
        st.close();
    }

    @AfterAll
    static void closeConnection() throws Exception {
        if (DB.get() != null && !DB.get().isClosed()) {
            DB.get().close();
        }
    }

    @Test
    @Order(1)
    void addStudentShouldInsertIntoDatabase() {

        repo.add(createStudent("S100", 3.0));

        assertTrue(repo.exists("S100"));
    }

    @Test
    @Order(2)
    void getAllShouldReturnInsertedStudents() {

        repo.add(createStudent("S200", 3.2));

        List<Student> list = repo.getAll();

        assertEquals(1, list.size());
    }

    @Test
    @Order(3)
    void deleteShouldRemoveStudent() {

        repo.add(createStudent("S300", 3.1));
        repo.delete("S300");

        assertFalse(repo.exists("S300"));
    }

    @Test
    @Order(4)
    void updateShouldModifyStudentGpa() {

        repo.add(createStudent("S400", 2.5));

        Student updated = createStudent("S400", 3.8);
        repo.update(updated);

        List<Student> list = repo.getAll();

        assertEquals(3.8, list.get(0).getGpa());
    }
}