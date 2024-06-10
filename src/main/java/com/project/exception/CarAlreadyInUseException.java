package com.project.exception;

public class CarAlreadyInUseException extends Throwable {

  public CarAlreadyInUseException(Long carId) {
    super("Car with id " + carId + " is already in use.");
  }
}
