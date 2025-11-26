package org.delarosa.app.empleado.doctor;

public class EspecialidadNoExistente extends IllegalArgumentException {
    public EspecialidadNoExistente(String message) {
        super(message);
    }
}
