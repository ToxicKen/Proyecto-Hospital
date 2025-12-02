package org.delarosa.app.paciente;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;

import java.util.List;

public record PacienteDTO(PersonaDTO personaDTO,
                          RegistroUsuarioRequest registroUsuarioRequest,
                          List<Integer> idAlergias,
                          List<String> nuevasAlergias,
                          List<PadecimientoDTO> padecimientos,
                          HistorialMedicoDTO historialMedico

) {


}
