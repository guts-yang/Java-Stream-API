package com.learn.stream.model;

import java.util.Objects;

/**
 * 课程模型类
 * 用于演示Stream API对复杂对象的操作
 */
public class Course {
    private String courseName;
    private String instructor;
    private int credit;
    private double score;
    private String category;
    
    public Course(String courseName, String instructor, int credit, double score, String category) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.credit = credit;
        this.score = score;
        this.category = category;
    }
    
    // Getters and Setters
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    
    public int getCredit() { return credit; }
    public void setCredit(int credit) { this.credit = credit; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseName, course.courseName) &&
               Objects.equals(instructor, course.instructor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseName, instructor);
    }
    
    @Override
    public String toString() {
        return String.format("Course{courseName='%s', instructor='%s', credit=%d, score=%.1f, category='%s'}", 
                           courseName, instructor, credit, score, category);
    }
}