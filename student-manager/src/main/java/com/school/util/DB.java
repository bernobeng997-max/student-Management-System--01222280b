package com.school.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static Connection conn;

    public static Connection get() {

        try {
            if (conn == null || conn.isClosed()) {
                init();
            }
            return conn;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init() {

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:data/students.db");

            conn.createStatement().execute("""
                CREATE TABLE IF NOT EXISTS students(
                student_id TEXT PRIMARY KEY,
                full_name TEXT NOT NULL,
                programme TEXT NOT NULL,
                level INTEGER,
                gpa REAL,
                email TEXT,
                phone TEXT,
                date_added TEXT,
                status TEXT
                )
            """);

            System.out.println("Database ready");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
