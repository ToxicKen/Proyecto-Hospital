package org.delarosa.app.modules.personal.dtos;

import java.util.List;

public record DoctorDatosResponse(Integer idDoctor, String nombreCompleto, String especialidad, Integer Consultorio, Boolean activo , List<HorarioEmpleadoDTO> horarios) {
}
