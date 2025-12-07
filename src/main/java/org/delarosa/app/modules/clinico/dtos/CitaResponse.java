package org.delarosa.app.modules.clinico.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CitaResponse(
        Integer folioCita,
        LocalDateTime fechaCita,
        String estatus,
        String nombrePaciente,
        String nombreDoctor,
        String especialidad,
        Integer consultorio,
        BigDecimal costo
) {}