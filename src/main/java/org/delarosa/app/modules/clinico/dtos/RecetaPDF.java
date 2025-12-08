package org.delarosa.app.modules.clinico.dtos;

import org.delarosa.app.modules.clinico.entities.Receta;

public record RecetaPDF(String pacienteNombre, String doctorNombre,String fechaEmision, RecetaRequest recetaRequest) {
}
