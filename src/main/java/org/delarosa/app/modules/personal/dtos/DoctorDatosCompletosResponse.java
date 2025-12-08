package org.delarosa.app.modules.personal.dtos;

import org.delarosa.app.modules.general.dtos.PersonaResponse;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;

import java.util.List;

public record DoctorDatosCompletosResponse(PersonaResponse persona, List<HorarioEmpleadoDTO> horarios, String email, String especialidad, Integer Consultorio, String cedulaProfesional) {
}
