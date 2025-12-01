package org.delarosa.app.paciente;

import org.delarosa.app.modules.security.dto.AuthResponse;

public interface PacienteService {
    AuthResponse registrarPaciente(PacienteDTO pacienteDTO);
    Paciente crearPaciente(PacienteDTO pacienteDTO);
    Paciente obtenerPacienteDesdeToken(String token);
    PacienteDatosDTO obtenerDatosPaciente(String token);
    Paciente obtenerPacienteById(Integer id);
    Paciente buscarPorCorreo(String email);
}
