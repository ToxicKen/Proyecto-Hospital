package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record MedicamentoResponse(Integer idMedicamento, String nombre, BigDecimal precio,Integer stock) {
}
