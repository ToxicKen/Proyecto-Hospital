package org.delarosa.app.modules.paciente.services;

import org.delarosa.app.modules.paciente.dtos.ActualizarPacienteRequest;
import org.delarosa.app.modules.paciente.dtos.*;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.security.dto.AuthResponse;

import java.util.List;

public interface PacienteService {
    AuthResponse registrarPaciente(RegistroPacienteRequest pacienteDTO);

    Paciente crearPaciente(RegistroPacienteRequest pacienteDTO);

    Paciente obtenerPacienteDesdeToken(String token);

    PacienteResponse obtenerDatosPaciente(String Correo);

    Paciente obtenerPacienteById(Integer id);

    Paciente obtenerPacienteByCorreo(String email);

    List<PadecimientoExistenteDTO> obtenerPadecimientosExistentes();

    List<AlergiaExistenteDTO> obtenerAlergiasExistentes();

    PacienteResponse actualizarPaciente(String Correo, ActualizarPacienteRequest dto);
}
