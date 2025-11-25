package org.delarosa.app.paciente;

public class AlergiaNoExistente extends RuntimeException {
    public AlergiaNoExistente(String message) {
        super(message);
    }
}
