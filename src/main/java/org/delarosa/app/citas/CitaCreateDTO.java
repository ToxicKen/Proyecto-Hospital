package org.delarosa.app.citas;

import java.time.LocalDateTime;

public record CitaCreateDTO(Integer idDoctor, LocalDateTime fechaCita) {
}
