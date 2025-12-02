package org.delarosa.app.paciente;

public class PadecimientoNoExistente extends RuntimeException {
    public PadecimientoNoExistente(String message) {
        super(message);
    }
}
