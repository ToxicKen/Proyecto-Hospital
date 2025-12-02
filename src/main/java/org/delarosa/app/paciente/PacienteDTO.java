package org.delarosa.app.paciente;

import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;

import java.util.List;

public record PacienteDTO(RegistroPersonaRequest registroPersonaRequest,
                          RegistroUsuarioRequest registroUsuarioRequest,
                          List<Integer> idAlergias,
                          List<String> nuevasAlergias,
                          List<PadecimientoDTO> padecimientos,
                          HistorialMedicoDTO historialMedico

) {


}
