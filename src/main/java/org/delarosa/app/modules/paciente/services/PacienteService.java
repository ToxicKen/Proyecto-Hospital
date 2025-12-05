package org.delarosa.app.modules.paciente.services;

import org.delarosa.app.modules.paciente.dtos.PacienteResponse;
import org.delarosa.app.modules.paciente.dtos.RegistroPacienteRequest;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface PacienteService {
    AuthResponse registrarPaciente(RegistroPacienteRequest pacienteDTO);

    Paciente crearPaciente(RegistroPacienteRequest pacienteDTO);

    Paciente obtenerPacienteDesdeToken(String token);

    PacienteResponse obtenerDatosPaciente(String token);

    Paciente obtenerPacienteById(Integer id);

    Paciente buscarPorCorreo(String email);
}
