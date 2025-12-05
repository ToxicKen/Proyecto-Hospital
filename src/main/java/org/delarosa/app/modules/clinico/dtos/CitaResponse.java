package org.delarosa.app.modules.clinico.dtos;

import java.time.LocalDateTime;

public record CitaResponse(Integer folioCita, String nombrePaciente, String nombreDoctor, String especialdiad , Integer Consultorio,
                           LocalDateTime fechaCita, String estatus) {
}
