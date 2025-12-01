package org.delarosa.app.empleado.doctor;

public class DoctorNoEncontradoException extends RuntimeException {
  public DoctorNoEncontradoException(String message) {
    super(message);
  }
}
