package com.school.repository;

import com.school.domain.Student;
import com.school.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteStudentRepository implements StudentRepository {

    @Override
    public void add(Student s) {

        String sql = """
        INSERT INTO students
        (student_id, full_name, programme, level, gpa, email, phone, date_added, status)
        VALUES (?,?,?,?,?,?,?,?,?)
        """;

        try {

            PreparedStatement ps = DB.get().prepareStatement(sql);

            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getFullName());
            ps.setString(3, s.getProgramme());
            ps.setInt(4, s.getLevel());
            ps.setDouble(5, s.getGpa());
            ps.setString(6, s.getEmail());
            ps.setString(7, s.getPhone());

            ps.setString(8, java.time.LocalDate.now().toString()); // auto date
            ps.setString(9, "ACTIVE"); // auto status

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Student> getAll() {

        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students";

        try {

            Statement st = DB.get().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                list.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("full_name"),
                        rs.getString("programme"),
                        rs.getInt("level"),
                        rs.getDouble("gpa"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("date_added"),
                        rs.getString("status")
                ));
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public void delete(String id) {

        try {

            PreparedStatement ps =
                    DB.get().prepareStatement("DELETE FROM students WHERE student_id=?");

            ps.setString(1, id);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String id) {

        try {

            PreparedStatement ps =
                    DB.get().prepareStatement("SELECT 1 FROM students WHERE student_id=?");

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            boolean found = rs.next();

            rs.close();
            ps.close();

            return found;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Student s) {

        String sql = """
        UPDATE students SET
        full_name=?,
        programme=?,
        level=?,
        gpa=?,
        email=?,
        phone=?,
        status=?
        WHERE student_id=?
        """;

        try (Connection c = DB.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getFullName());
            ps.setString(2, s.getProgramme());
            ps.setInt(3, s.getLevel());
            ps.setDouble(4, s.getGpa());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getPhone());
            ps.setString(7, s.getStatus());
            ps.setString(8, s.getStudentId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
