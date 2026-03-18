package com.pao.laboratory03.exercise.exception;

public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(double grade) {
        super("Notă invalidă: " + grade + ". Trebuie să fie între 1 și 10.");
    }
}
