package org.delarosa.app.empleado;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.usuario.UsuarioDTO;

import java.math.BigDecimal;

public record EmpleadoDTO(PersonaDTO personaDTO, UsuarioDTO usuarioDTO,
                          BigDecimal salario) {
}
