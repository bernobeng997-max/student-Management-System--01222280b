package com.school.repository;

import com.school.domain.Student;
import java.util.List;

public interface StudentRepository {

    void add(Student s);

    void delete(String id);

    void update(Student s);

    List<Student> getAll();

    boolean exists(String id);

    // Added for dashboard compatibility (does not break existing code)
    default List<Student> findAll() {
        return getAll();
    }
}