package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record DetalleMedicamentoResponse(Integer idMedicamento, String nombre, Integer cantidad, BigDecimal precioUnitario,BigDecimal precioTotal) {
}
