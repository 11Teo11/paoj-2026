package com.pao.laboratory03.exercise.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String name) {
        super("Studentul '" + name + "' nu a fost găsit.");
    }
}
