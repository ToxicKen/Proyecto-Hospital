package org.delarosa.app;

import java.time.DayOfWeek;

public enum Dia {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    private final DayOfWeek day;

    Dia() {
        this.day = convertirDiaADay();
    }

    private DayOfWeek convertirDiaADay() {
        return switch (this) {
            case LUNES -> DayOfWeek.MONDAY;
            case MARTES -> DayOfWeek.TUESDAY;
            case MIERCOLES -> DayOfWeek.WEDNESDAY;
            case JUEVES -> DayOfWeek.THURSDAY;
            case VIERNES -> DayOfWeek.FRIDAY;
            case SABADO -> DayOfWeek.SATURDAY;
            case DOMINGO -> DayOfWeek.SUNDAY;
        };
    }

    public DayOfWeek getDay() {
        return day;
    }
}
