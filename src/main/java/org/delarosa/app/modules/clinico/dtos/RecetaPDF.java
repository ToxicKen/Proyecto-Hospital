package org.delarosa.app.modules.clinico.dtos;


public record RecetaPDF(Integer folioReceta,String pacienteNombre, String doctorNombre,String fechaEmision, RecetaRequest recetaRequest) {
}
