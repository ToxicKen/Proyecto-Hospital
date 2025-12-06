package org.delarosa.app.modules.personal.services;

import org.delarosa.app.modules.general.enums.Dia;
import org.delarosa.app.modules.personal.dtos.RegistroEmpleadoRequest;
import org.delarosa.app.modules.personal.entities.Empleado;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface EmpleadoService {
    Empleado crearEmpleado(RegistroEmpleadoRequest registroEmpleadoRequest);

    List<HorarioEmpleado> obtenerHorariosDeEmpleadoByIdEmpleado(Integer idEmpleado);

    List<Dia> obtenerDiasLaboralesByIdEmpleado(Integer idEmpleado);

}
