package org.delarosa.app.paciente;

import org.delarosa.app.persona.PersonaDTO;

import java.util.List;

public record PacienteDatosDTO(PersonaDTO personaDTO, String email, List<String> alergias,List<PadecimientoDatosDTO> padecimientos,HistorialMedicoDTO historialMedico) {
}
