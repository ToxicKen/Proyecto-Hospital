package org.delarosa.app.empleado.doctor;

public class ConsultorioNoExistenteExecption extends RuntimeException {
  public ConsultorioNoExistenteExecption(String message) {
    super(message);
  }
}
