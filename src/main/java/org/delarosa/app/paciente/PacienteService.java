package org.delarosa.app.paciente;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.security.auth.AuthResponse;

public interface PacienteService {
    AuthResponse registrarPaciente(Paciente paciente);
    Paciente crearPaciente(PacienteDTO pacienteDTO);
}
