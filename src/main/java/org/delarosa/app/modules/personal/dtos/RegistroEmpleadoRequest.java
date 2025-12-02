package org.delarosa.app.modules.personal.dtos;

import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.security.dto.RegistroUsuarioRequest;

import java.math.BigDecimal;

public record RegistroEmpleadoRequest(RegistroPersonaRequest registroPersonaRequest, RegistroUsuarioRequest registroUsuarioRequest,
                                      BigDecimal salario) {
}
