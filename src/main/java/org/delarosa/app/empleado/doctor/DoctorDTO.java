package org.delarosa.app.empleado.doctor;

import org.delarosa.app.empleado.EmpleadoDTO;
import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.usuario.UsuarioDTO;

import java.math.BigDecimal;

public record DoctorDTO(EmpleadoDTO empleadoDTO, String cedulaProfesional, Integer idConsultorio, Integer idEspecialidad) {
}
