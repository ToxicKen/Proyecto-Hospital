package org.delarosa.app.modules.personal.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record HorarioEmpleadoDTO(String dia, LocalTime hraEntrada, LocalTime hraSalida) {
}
