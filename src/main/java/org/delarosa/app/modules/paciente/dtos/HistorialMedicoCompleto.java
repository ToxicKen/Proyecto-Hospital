package org.delarosa.app.modules.paciente.dtos;

import org.delarosa.app.modules.clinico.dtos.BitacoraResumenDTO;

import java.util.List;

public record HistorialMedicoCompleto(PacienteResponse pacienteResponse, List<BitacoraResumenDTO> citas) {
}
