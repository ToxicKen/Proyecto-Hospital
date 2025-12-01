package org.delarosa.app;

public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String message) {
        super(message);
    }
}
