package org.delarosa.app.paciente;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.usuario.UsuarioDTO;

import java.util.List;

public record PacienteDTO(PersonaDTO personaDTO,
                          UsuarioDTO usuarioDTO,
                          List<Integer> idAlergias,
                          List<String> nuevasAlergias,
                          List<PadecimientoDTO> padecimientos,
                          HistorialMedicoDTO historialMedico

) {


}
