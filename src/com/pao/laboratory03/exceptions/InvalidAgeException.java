package com.pao.laboratory03.exceptions;

public class InvalidAgeException extends RuntimeException {
  public InvalidAgeException(int age)
  {
      super("Vârsta " + age + " nu este validă (0-150)");
  }
}
