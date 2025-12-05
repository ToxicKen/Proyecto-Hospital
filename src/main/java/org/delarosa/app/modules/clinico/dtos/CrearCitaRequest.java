package org.delarosa.app.modules.clinico.dtos;

import java.time.LocalDateTime;

public record CrearCitaRequest(Integer idDoctor, LocalDateTime fechaCita) {
}
