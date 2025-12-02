package org.delarosa.app.modules.personal.dtos;

public record RegistroDoctorRequest(RegistroEmpleadoRequest registroEmpleadoRequest, String cedulaProfesional, Integer idConsultorio, Integer idEspecialidad) {
}
