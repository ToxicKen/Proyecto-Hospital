package org.delarosa.app.empleado;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;

import java.math.BigDecimal;

public record EmpleadoDTO(PersonaDTO personaDTO, RegistroUsuarioRequest registroUsuarioRequest,
                          BigDecimal salario) {
}
