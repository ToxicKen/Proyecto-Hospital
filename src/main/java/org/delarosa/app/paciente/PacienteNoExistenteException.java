package org.delarosa.app.paciente;

public class PacienteNoExistenteException extends RuntimeException {
    public PacienteNoExistenteException(String message) {
        super(message);
    }
}
