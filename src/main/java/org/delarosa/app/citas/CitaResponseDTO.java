package org.delarosa.app.citas;

import java.time.LocalDateTime;

public record CitaResponseDTO(Integer folioCita, String nombrePaciente, String nombreDoctor, String especialdiad , Integer Consultorio,
                              LocalDateTime fechaCita,String estatus) {
}
