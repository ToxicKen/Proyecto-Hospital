package org.delarosa.app.paciente;

public class PacienteNoEncontradoException extends RuntimeException {
    public PacienteNoEncontradoException(String message) {
        super(message);
    }
}
