package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Empleado;

public interface EmpleadoService {
    Empleado crearEmpleado(RegistroEmpleadoRequest registroEmpleadoRequest);
}
