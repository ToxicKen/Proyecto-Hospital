package org.delarosa.app.modules.farmacia.dtos;

import java.math.BigDecimal;

public record MedicamentoEditRequest(String nombre, String descripcion, BigDecimal precio) {
}
