package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record MedicamentoRegistroRequest(String nombre, BigDecimal precio, Integer cantidad) {
}
