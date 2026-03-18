package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.*;

public class StudentService {
    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    private static class Holder {
        private static final StudentService INSTANCE = new StudentService();
    }

    public static StudentService getInstance() {
        return Holder.INSTANCE;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja.");
            }
        }
        Student s= new Student(name, age);
        students.add(s);
    }


    public Student findByName(String name) {
        for (Student s : students)
            if (s.getName().equals(name))
                return s;

        throw new StudentNotFoundException(name);
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }
        int cnt = 1;
        for (Student s : students) {
            System.out.println(cnt + ". " + s.toString());
            cnt += 1;
            for (Map.Entry<Subject, Double> e : s.getGrades().entrySet()) {
                System.out.println("\t" + e.getKey().name() + " = " + e.getValue());
            }
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti inregistrati.");
            return;
        }
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((a, b) -> Double.compare(b.getAverage(), a.getAverage()));

        System.out.println("=== Top Studenți ===");
        int cnt = 1;
        for (Student s : sorted) {
            System.out.println(cnt + ". " + s.getName() + " - media: " + s.getAverage());
            cnt += 1;
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> medie = new HashMap<>();
        Map<Subject,Integer> nr = new HashMap<>();

        for (Student s: students){
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()){
                Subject subject= entry.getKey();
                double grade= entry.getValue();

                medie.put(subject, medie.getOrDefault(subject, 0.0)+grade);
                nr.put(subject, nr.getOrDefault(subject, 0)+1);
            }
        }

        Map<Subject, Double> note = new HashMap<>();

        for(Subject s: medie.keySet())
            note.put (s, medie.get(s)/nr.get(s));

        return note;
    }
}
