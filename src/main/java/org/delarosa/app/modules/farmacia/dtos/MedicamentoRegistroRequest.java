package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record MedicamentoRegistoroRequest(String nombre, BigDecimal precio, Integer cantidad) {
}
