package com.school.domain;

public class Student {

    private String studentId;
    private String fullName;
    private String programme;
    private int level;
    private double gpa;
    private String email;
    private String phone;
    private String dateAdded;
    private String status;

    public Student(String studentId,
                   String fullName,
                   String programme,
                   int level,
                   double gpa,
                   String email,
                   String phone,
                   String dateAdded,
                   String status) {

        this.studentId = studentId;
        this.fullName = fullName;
        this.programme = programme;
        this.level = level;
        this.gpa = gpa;
        this.email = email;
        this.phone = phone;
        this.dateAdded = dateAdded;
        this.status = status;
    }

    // CSV shortcut constructor
    public Student(String id,
                   String name,
                   String programme,
                   int level,
                   double gpa,
                   String email) {

        this(id, name, programme, level, gpa, email, "", "", "ACTIVE");
    }


    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getProgramme() { return programme; }
    public int getLevel() { return level; }
    public double getGpa() { return gpa; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateAdded() { return dateAdded; }
    public String getStatus() { return status; }

    public void setDateAdded(String d){ this.dateAdded=d; }
    public void setStatus(String s){ this.status=s; }
}





