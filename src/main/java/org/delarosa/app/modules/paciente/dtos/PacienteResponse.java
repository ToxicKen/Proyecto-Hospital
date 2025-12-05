package org.delarosa.app.modules.paciente.dtos;
import org.delarosa.app.modules.general.dtos.PersonaResponse;

import java.util.List;

public record PacienteResponse(PersonaResponse persona, String email, List<String> alergias, List<PadecimientoDatosDTO> padecimientos, HistorialMedicoDTO historialMedico) {
}
