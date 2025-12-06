package org.delarosa.app.modules.clinico.services;


import org.delarosa.app.modules.clinico.dtos.CitaResponse;
import org.delarosa.app.modules.clinico.dtos.CrearCitaRequest;
import org.delarosa.app.modules.clinico.dtos.EspecialidadDTO;
import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.paciente.entities.Paciente;

import java.math.BigDecimal;
import java.util.List;

public interface CitaService {
    CitaResponse crearCita(CrearCitaRequest crearCitaRequest, Paciente paciente);

    Cita obtenerById(Integer id);
    
}
