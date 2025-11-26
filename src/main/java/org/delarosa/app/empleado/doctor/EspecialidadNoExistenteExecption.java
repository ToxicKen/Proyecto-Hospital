package org.delarosa.app.empleado.doctor;

public class EspecialidadNoExistenteExecption extends IllegalArgumentException {
    public EspecialidadNoExistenteExecption(String message) {
        super(message);
    }
}
