package org.delarosa.app.modules.clinico.dtos;


import java.util.List;

public record RecetaRequest(Integer folioCita, String diagnostico, String observaciones, List<MedicamentosRecetaRequest> medicamentos) {
}
