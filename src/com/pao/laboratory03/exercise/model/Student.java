package com.pao.laboratory03.exercise.model;
import com.pao.laboratory03.exercise.exception.InvalidGradeException;
import com.pao.laboratory03.exercise.exception.InvalidStudentException;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;

    public Student(String name, int age){
        this.name = name;
        if (age < 18 || age > 60)
            throw new InvalidStudentException(age);
        this.age = age;
        grades = new HashMap<>();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public Map<Subject, Double> getGrades() { return grades; }

    public void addGrade(Subject subject, double grade){
        if (grade < 1 || grade > 10)
            throw new InvalidGradeException(grade);
        grades.put(subject,grade);
    }

    public double getAverage(){
        if (grades.isEmpty())
            return 0;
        double medie = 0;
        for (double g : grades.values())
            medie += g;
        return medie/grades.size();
    }

    @Override
    public String toString(){
        return String.format("Student{name='" + getName() + "', age=" + getAge() + ", avg=%.2f}", getAverage());
    }

}
