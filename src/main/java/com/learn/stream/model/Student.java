package com.learn.stream.model;

/**
 * 学生模型类
 * 用于演示Stream API对对象集合的操作
 */
public class Student {
    private String name;
    private int age;
    private String major;
    private double gpa;
    private String gender;
    
    public Student(String name, int age, String major, double gpa, String gender) {
        this.name = name;
        this.age = age;
        this.major = major;
        this.gpa = gpa;
        this.gender = gender;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    @Override
    public String toString() {
        return String.format("Student{name='%s', age=%d, major='%s', gpa=%.2f, gender='%s'}", 
                           name, age, major, gpa, gender);
    }
}