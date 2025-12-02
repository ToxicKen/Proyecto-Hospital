package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface RecepcionistaService {
    AuthResponse registrarRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest);

    Recepcionista crearRecepcionista(RegistroEmpleadoRequest registroEmpleadoRequest);
}
