package com.pao.laboratory03.exercise.exception;

public class InvalidStudentException extends RuntimeException {
    public InvalidStudentException(int age) {

      super("Vârsta invalidă: " + age + ". Trebuie să fie între 18 și 60.");
    }
}
