package org.delarosa.app.citas;


import org.delarosa.app.paciente.Paciente;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface CitaService {
    CitaResponseDTO crearCita(CitaCreateDTO citaCreateDTO, Paciente paciente);
    Cita obtenerById(Integer id);
    BigDecimal obtenerMontoDeCita(Cita cita);
}
