package org.delarosa.app.paciente;

import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;

import java.util.List;

public record PacienteDatosDTO(RegistroPersonaRequest registroPersonaRequest, String email, List<String> alergias, List<PadecimientoDatosDTO> padecimientos, HistorialMedicoDTO historialMedico) {
}
