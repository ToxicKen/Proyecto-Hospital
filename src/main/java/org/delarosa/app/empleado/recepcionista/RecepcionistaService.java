package org.delarosa.app.empleado.recepcionista;

import org.delarosa.app.empleado.EmpleadoDTO;
import org.delarosa.app.modules.security.dto.AuthResponse;

public interface RecepcionistaService {
    AuthResponse registrarRecepcionista(EmpleadoDTO empleadoDTO);
    Recepcionista crearRecepcionista(EmpleadoDTO empleadoDTO);
}
